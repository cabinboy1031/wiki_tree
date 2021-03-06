package com.cabinboy1031.wiki_tree;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**LinkTree works as a backbone structure to the whole program to show relationships to each other.
 * @author Cabinboy1031
 *
 */
public class LinkTree {
    public String Name;
    List<LinkTree> ListLinks = new ArrayList<>();
    /**Adds a new node based on String input. If the link is already added to the list, then it is not added again, making sure every link is unique,
     * @param Name: The topic that connects to the current node.
     * @return LinkRef: A reference to the added link. Can be null if not added.
     */
    public LinkTree addLink(String Name){
        if(!this.hasLink(Name)) {
            LinkTree NewNode = new LinkTree();
            NewNode.Name = Name;
            this.ListLinks.add(NewNode);
            return NewNode;
        }
        return null;
    }

    /**Displays links in order of their position on the tree. Root, link 1, link 1,1, link 1,2, link 2, link 2,1 etc.
     * @param CurrentRoot Just pass a "" in here unless you like distinguishing between root and branches.
     */
    public void displayLinks(String CurrentRoot){
        StringBuilder NextNode = new StringBuilder(CurrentRoot + "~");
        System.out.println(CurrentRoot + this.Name);
        for (LinkTree Item: this.ListLinks) {
            Item.displayLinks(NextNode.toString());
        }
    }

    /**
     * @param index Link node at index.
     * @return The link node at index. Duh.
     */
    public LinkTree getLinkAtIndex(int index){
        return this.ListLinks.get(index);
    }

    /**
     * @param Name Name of the link. Mainly used for searching nodes for a copies to clean up.
     * @return Link node of the same name.
     * @throws Throwable In case you misspell. YOu dont want things crashing over a typo. Returns an empty linkTree object instead.
     */
    public LinkTree getLinkByName(String Name){
        Throwable NameNotFoundException = new Throwable("Name not found! Did you spell things right? Did the link fuck up?");
        try {
            for (LinkTree Item : ListLinks) {
                if (Item.Name.equals(Name)) {
                    return Item;
                } else {
                    continue;
                }
            }
            throw NameNotFoundException;
        }catch (Throwable e) {
            e.printStackTrace();
            return new LinkTree();
        }
    }

    /**Returns true if the link is in the current node
     *
     * @param Name
     * @return boolean
     */
    public boolean hasLink(String Name){
        for(LinkTree Item: ListLinks){
            if (Item.Name.equals(Name)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }


}