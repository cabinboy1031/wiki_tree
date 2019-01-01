package com.cabinboy1031.wiki_tree;
import java.util.List;

/** Program that takes any wikipedia link and then displays a graph showing what topics are related to it based on the links of the HTML file.
 * @author Cabinboy1031
 */
public class App 
{
    public static void main( String[] args )
    {
        //Init Variables
        HTMLLinkParser Parser = new HTMLLinkParser();
        LinkTree RootTree = new LinkTree();

        //First HTTP request
        final String Url = "http://www.wikipedia.com/wiki/";
        StringBuilder Topic = new StringBuilder("Spartacus");
        RootTree.Name = Topic.toString();
        StringBuilder Doc = Parser.HTTPRequest(Url,Topic);
        //System.out.println(Doc);

        //Link Parsing.
        List<String> ListLinks = Parser.collectLinks(Doc);
        List<StringBuilder> CleanedLinks = Parser.validateLinks(Topic.toString(), ListLinks);
        for (StringBuilder Link: CleanedLinks) {
            //System.out.println(Link);
            //TODO Feeding stripped links into the LinkTree root
            RootTree.addLink(Link.toString());
        }
        RootTree.displayLinks("|");
        //TODO Recursion over entire LinkTree Structure, going through every link to collect the second row of links. Repeat as far as possible

        //TODO Display links with a 2D display library and their connection to the root.
    }

}