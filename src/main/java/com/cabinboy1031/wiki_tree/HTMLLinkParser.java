package com.cabinboy1031.wiki_tree;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLLinkParser {
    /** Sends a GET request to the Url and Topic.
     * @param Url: Https://example.com/, Ideally immutable to prevent going off the desired site.
     * @param Topic The things after the URL. /Spartacus/ this is the part that will change a lot.
     * @return StringBuilder of the HTML Document Body
     */
    public StringBuilder HTTPRequest(String Url, StringBuilder Topic) {
        try {
            //Shows progress for the user.
            System.out.println("Request:" + Topic);
            //Take the document at this address and then strip to prevent links to appear from the head
            Document doc=        Jsoup.connect(Url +Topic).get();
            StringBuilder body = stripDocument(doc);

            System.out.println("Success");
            return body;
        } catch (IOException e) {
            //Return null if link is a dead end.
            System.err.println("Directory not found:" + e);
            return null;
        }
    }

    /**
     * @param Doc HTML doc.
     * @return String List of links from the document.
     */
    public List<String> collectLinks(StringBuilder Doc){
        Document GivenDoc = Jsoup.parse(Doc.toString());
        Elements links = GivenDoc.select("a[href]");
        return links.eachAttr("href");
    }

    /** Removes the header part from the html document
     * @param GivenDoc:HTML doc. Why is this one not a StringBuilder? Fuck you that is why.
     * @return String List of links from the document.
     */
    private StringBuilder stripDocument(Document GivenDoc){
        //Document GivenDoc = Jsoup.parse(Doc.toString());
        StringBuilder StrippedDocument = new StringBuilder(GivenDoc.body().toString());
        return StrippedDocument;
    }

    /** Arguably the most important bit. Validates, filters, and makes a list of all the desired links.
     * @param Topic: Basically just the topic to prevent the links from recursing over a single stage.
     * @param ListLinks: What the collectLinks() function feeds into.
     * @return Finalised list of links to feed into the LinkTree class.
     */
    public List<StringBuilder> validateLinks(String Topic, List<String> ListLinks){
        Pattern[] InvalidLinks = {
                Pattern.compile("^/wiki/Wikipedia:*"),
                Pattern.compile("^/wiki/File:*"),
                Pattern.compile("^/wiki/Special:*"),
                Pattern.compile("^/wiki/Main_Page$"),
                Pattern.compile("^/wiki/Help:*"),
                Pattern.compile("^/wiki/Portal*"),
                Pattern.compile("^/wiki/Category:"),
                Pattern.compile("^/wiki/Talk:*"),
                Pattern.compile("^/wiki/"+ Topic)
        };

        Pattern ValidLink = Pattern.compile("^/wiki/*");
        Matcher isValid;
        Matcher notValid;

        List<StringBuilder> ValidLinks = new ArrayList<>();
        //Loop iterating over the links
        for(String Item: ListLinks) {
            isValid = ValidLink.matcher(Item);
            if (isValid.find()) {
                //Loop iterating over the regex list
                boolean LoopBroke = false;
                for (Pattern Link : InvalidLinks) {
                    notValid = Link.matcher(Item);

                    if (!notValid.find()) {
                        continue;
                    } else {
                        LoopBroke = true;
                        break;
                    }
                }

                if (!LoopBroke) {
                    StringBuilder CurrentLink = new StringBuilder(Item);
                    if(!ValidLinks.contains(CurrentLink)) {
                        ValidLinks.add(CurrentLink.delete(0, 6));
                    }
                }
            }
        }
        return ValidLinks;
    }

    private LinkTreeFileWriter FileWriter = new LinkTreeFileWriter("/home/Output.txt");
    public void writeLinksToFile(String CurrentRoot, LinkTree CurrentItem){
        StringBuilder NextNode = new StringBuilder(CurrentRoot + "~");
        FileWriter.writeToFile(CurrentRoot + CurrentItem.Name);
        for (LinkTree Item: CurrentItem.ListLinks) {
            this.writeLinksToFile(NextNode.toString(), Item);
        }
    }
}
