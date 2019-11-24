package com.cabinboy1031.wiki_tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/** Program that takes any wikipedia link and then displays a graph showing what topics are related to it based on the links of the HTML file.
 * @author Cabinboy1031
 */
public class App 
{
    public static void main( String[] args )
    {



        final int TREE_MAX_SIZE = 10000; // When the tree hits max size, it will stop loading things to the queue.
        final String ROOT_TOPIC = ""; // The root topic is where the first page is loaded.



        //Init Variables
        HTMLLinkParser Parser = new HTMLLinkParser();
        LinkTree RootTree =     new LinkTree();
        RootTree.Name =         ROOT_TOPIC;
        int treeCurrentSize = 1;
        //Required global variables
        final String Url =    "http://www.wikipedia.com/wiki/";
        StringBuilder Topic = new StringBuilder(ROOT_TOPIC);
        StringBuilder Doc;
        List<StringBuilder> uniqueLinks = new ArrayList<>(); //Links already seen to prevent pages we already saw from loading.
        Queue<LinkTree> linkQueue = new ArrayDeque(); //Queue of links to download in a specified order
        linkQueue.add(RootTree);

        //Link Parsing.
        LinkTree newNode;
        List<String> ListLinks;
        List<StringBuilder> CleanedLinks;

        //RootTree.displayLinks("|");
        while(!linkQueue.isEmpty()){
            LinkTree Link = linkQueue.remove();
            //Replace Topic with the item name
            Topic.delete(0,Topic.length());
            Topic.append(Link.Name);

            //Replace list of strings with new list.
            Doc =           Parser.HTTPRequest(Url, Topic);
            ListLinks =     Parser.collectLinks(Doc);
            CleanedLinks =  Parser.validateLinks(Topic.toString(), ListLinks, uniqueLinks);

            //Add list to the current node.
            for (StringBuilder CleanLink: CleanedLinks) {
                newNode = Link.addLink(CleanLink.toString());
                if (newNode != null && treeCurrentSize <= TREE_MAX_SIZE){
                    linkQueue.add(newNode);
                    treeCurrentSize++;
                }
            }
        }
        Parser.writeLinksToFile("|", RootTree);
        //RootTree.displayLinks("~");

        //TODO Display links with a 2D display library and their connection to the root.
    }

}