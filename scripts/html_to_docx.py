#!/usr/bin/env python3
"""
Convert a simple HTML (our .doc HTML wrapper) into a .docx using python-docx.
This is intentionally minimal: supports headings, paragraphs, bold/italic,
unordered/ordered lists, and basic tables.
"""

from __future__ import annotations

import sys
from pathlib import Path

from bs4 import BeautifulSoup, NavigableString, Tag
from docx import Document
from docx.enum.text import WD_ALIGN_PARAGRAPH


def _add_runs_from_node(paragraph, node: Tag | NavigableString, bold=False, italic=False):
    if isinstance(node, NavigableString):
        text = str(node)
        if text:
            run = paragraph.add_run(text)
            run.bold = bold
            run.italic = italic
        return

    if not isinstance(node, Tag):
        return

    name = node.name.lower()
    if name in {"b", "strong"}:
        for child in node.children:
            _add_runs_from_node(paragraph, child, bold=True or bold, italic=italic)
        return
    if name in {"i", "em"}:
        for child in node.children:
            _add_runs_from_node(paragraph, child, bold=bold, italic=True or italic)
        return
    if name == "br":
        paragraph.add_run("\n")
        return

    # default: recurse
    for child in node.children:
        _add_runs_from_node(paragraph, child, bold=bold, italic=italic)


def _text_of(tag: Tag) -> str:
    return tag.get_text(" ", strip=True)


def _handle_table(doc: Document, table_tag: Tag):
    rows = table_tag.find_all("tr", recursive=False)
    if not rows:
        return
    # Determine columns from first row cells (th/td)
    first_cells = rows[0].find_all(["th", "td"], recursive=False)
    cols = max(1, len(first_cells))
    table = doc.add_table(rows=0, cols=cols)
    table.style = "Table Grid"

    for r in rows:
        cells = r.find_all(["th", "td"], recursive=False)
        row = table.add_row().cells
        for i in range(cols):
            if i >= len(cells):
                continue
            cell_tag = cells[i]
            # clear default paragraph and add text
            row[i].text = _text_of(cell_tag)


def _handle_list(doc: Document, list_tag: Tag, ordered: bool):
    style = "List Number" if ordered else "List Bullet"
    for li in list_tag.find_all("li", recursive=False):
        p = doc.add_paragraph(style=style)
        # handle nested tags inside li (bold/italic)
        for child in li.children:
            _add_runs_from_node(p, child)


def _handle_block(doc: Document, el: Tag):
    name = el.name.lower()
    if name in {"h1", "h2", "h3", "h4"}:
        level = int(name[1])
        p = doc.add_paragraph(_text_of(el))
        p.style = f"Heading {min(level, 4)}"
        return

    if name == "p":
        text = _text_of(el)
        # skip empty spacer paragraphs
        if not text and not el.find(["b", "strong", "i", "em"]):
            return
        p = doc.add_paragraph()
        for child in el.children:
            _add_runs_from_node(p, child)
        return

    if name == "div":
        # callouts become paragraphs; keep content
        for child in el.children:
            if isinstance(child, Tag):
                _handle_block(doc, child)
        return

    if name == "ul":
        _handle_list(doc, el, ordered=False)
        return

    if name == "ol":
        _handle_list(doc, el, ordered=True)
        return

    if name == "table":
        _handle_table(doc, el)
        doc.add_paragraph()  # spacing
        return

    # Ignore head/style/script
    if name in {"style", "script", "head"}:
        return

    # Default: recurse
    for child in el.children:
        if isinstance(child, Tag):
            _handle_block(doc, child)


def html_to_docx(input_path: Path, output_path: Path):
    html = input_path.read_text(encoding="utf-8", errors="replace")
    soup = BeautifulSoup(html, "lxml")
    body = soup.body

    doc = Document()

    if body:
        for child in body.children:
            if isinstance(child, Tag):
                # Support manual page breaks via .pagebreak class on h2 or div
                if child.get("class") and "pagebreak" in child.get("class"):
                    doc.add_page_break()
                    # render the element after break
                    _handle_block(doc, child)
                else:
                    _handle_block(doc, child)
    else:
        doc.add_paragraph(soup.get_text(" ", strip=True))

    # Title centering is handled by heading styles; optionally center first paragraph if it matches title.
    if doc.paragraphs:
        doc.paragraphs[0].alignment = WD_ALIGN_PARAGRAPH.LEFT

    output_path.parent.mkdir(parents=True, exist_ok=True)
    doc.save(str(output_path))


def main(argv: list[str]) -> int:
    if len(argv) != 3:
        print("Usage: html_to_docx.py <input_html_doc> <output_docx>", file=sys.stderr)
        return 2
    input_path = Path(argv[1])
    output_path = Path(argv[2])
    if not input_path.exists():
        print(f"Input not found: {input_path}", file=sys.stderr)
        return 2
    html_to_docx(input_path, output_path)
    return 0


if __name__ == "__main__":
    raise SystemExit(main(sys.argv))

