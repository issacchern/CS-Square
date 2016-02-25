package com.chernyee.cssquare;

/**
 * Created by Issac on 2/24/2016.
 */
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;
import android.text.SpannableString;


public class SyntaxHighlighter {

    private final String[] JAVA_KEYWORDS =  { "abstract", "assert",
            "boolean", "break", "byte", "case", "catch", "char", "class",
            "const", "continue", "default", "do", "double", "else", "enum",
            "extends", "false", "final", "finally", "float", "for", "goto",
            "if", "implements", "import", "instanceof", "int", "interface",
            "long", "native", "new", "null", "package", "private", "protected",
            "public", "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws", "transient",
            "true", "try", "void", "volatile", "while" };

    private String content;

    public SyntaxHighlighter(String contents) {
        this.content = contents;
    }

    public SpannableString formatToHtml() {
        formatNewline();
        highlightKeywords();
        highlightComments();

        SpannableString spannableString = new SpannableString(
                Html.fromHtml(content));
        return spannableString;
    }

    private void formatNewline() {
        content = content.replaceAll("<", "&lt;");
        content = content.replaceAll(">", "&gt;");
        content = content.replaceAll("   ", "&nbsp;&nbsp;&nbsp;");
        content = content.replaceAll("\n\r", "<br />");
        content = content.replaceAll("\n", "<br />");
        content = content.replaceAll("\r", "<br />");

    }

    private void highlightKeywords() {
        for(String keyword: JAVA_KEYWORDS) {
            content = content.replaceAll("\\b" + keyword + "\\b", "<font color=\"purple\">" + keyword + "</font>");
        }
    }

    private void highlightComments() {
        content = content.replaceAll("/\\*.*?\\*/", "<font color=\"navy\">$0</font>");
        content = content.replaceAll("//[^\n]*?\n", "<font color=\"navy\">$0</font>");
    }

//    public void markupReferences(ArrayList<CscopeEntry> references) {
//        StringBuilder result = new StringBuilder(content);
//        for(CscopeEntry entry: references) {
//            Matcher matcher = Pattern.compile("(" + Pattern.quote(entry.actualName) + ")(?:\\s?\\()").matcher(result);
//
//            if(matcher.find()) {
//                result.insert(matcher.end(1), "</a>");
//                result.insert(matcher.start(1), "<a href=\"" + entry.actualName + "\">");
//            }
//        }
//        content = result.toString();
//    }
}