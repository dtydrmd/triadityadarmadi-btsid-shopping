package com.example.shop.support;

import java.util.Arrays;
import java.util.List;

public class SupportArticle {
    private String code;
    private String title;
    private String message;
    private String cause;
    private String resolution;
    private List<String> steps;
    private List<String> tcodes;

    public SupportArticle() {
    }

    public static SupportArticle forFaaPost124() {
        SupportArticle article = new SupportArticle();
        article.setCode("FAA_POST124");
        article.setTitle("Balance carryforward required for asset posting");
        article.setMessage("Balance carryforward is required for CoCode RPNA and ledger 0L in year 2026.");
        article.setCause(
                "Asset balance carryforward for fiscal year 2026 has not been executed for company code RPNA " +
                        "and ledger 0L. F-90 postings in prior years still require carryforward for the current year."
        );
        article.setResolution(
                "Complete year-end closing and run asset balance carryforward for 2026, then retry the F-90 posting."
        );
        article.setSteps(Arrays.asList(
                "Confirm posting periods for 2024 and 2026 are open in Asset Accounting.",
                "Finish depreciation run and year-end closing for 2025 (AJAB) if still open.",
                "Run asset balance carryforward for 2026 for CoCode RPNA, ledger 0L (AJRW or AFBN).",
                "Re-run F-90 for the 2024 acquisition."
        ));
        article.setTcodes(Arrays.asList("F-90", "AJAB", "AJRW", "AFBN"));
        return article;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public List<String> getTcodes() {
        return tcodes;
    }

    public void setTcodes(List<String> tcodes) {
        this.tcodes = tcodes;
    }
}
