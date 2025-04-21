
// -------------------------------------------------------------------------
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Main for Graph project (CS3114/CS5040 Spring 2025 Project 4).
 * Usage: java GraphProject <init-hash-size> <command-file>
 *
 * @author Yash Atal
 * @version 1.0
 *
 */

// On my honor:
// - I have not used source code obtained from another current or
// former student, or any other unauthorized source, either
// modified or unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class GraphProject {
    /**
     * @param args
     *            Command line parameters
     */
    private ExtensibleHashTable artistTable;
    private ExtensibleHashTable songTable;
    private Graph graph;

    public GraphProject(int initHashSize) {
        artistTable = new ExtensibleHashTable(initHashSize);
        songTable = new ExtensibleHashTable(initHashSize);
        graph = new Graph();
    }


    public void processCommand(String command) {
        String[] parts = command.split(" ", 2);
        String cmd = parts[0];
        String args = parts.length > 1 ? parts[1] : "";

        switch (cmd) {
            case "insert":
                processInsert(args);
                break;
            case "remove":
                processRemove(args);
                break;
            case "print":
                processPrint(args);
                break;
            default:
                System.out.println("Unknown command: " + cmd);
        }
    }


    private void processInsert(String args) {
        String[] parts = args.split("<SEP>");
        if (parts.length != 2) {
            System.out.println("Invalid insert format");
            return;
        }

        String artist = parts[0];
        String song = parts[1];

        // Handle artist
        GraphNode artistNode = artistTable.search(artist);
        if (artistNode == null) {
            artistNode = graph.addNode(artist, true);
            artistTable.insert(artist, artistNode);
        }

        // Handle song
        GraphNode songNode = songTable.search(song);
        if (songNode == null) {
            songNode = graph.addNode(song, false);
            songTable.insert(song, songNode);
        }

        // Add edge
        graph.addEdge(artistNode, songNode);
        System.out.println("Inserted " + artist + " and " + song
            + " into the database");
    }


    private void processRemove(String args) {
        String[] parts = args.split(" ", 2);
        if (parts.length != 2) {
            System.out.println("Invalid remove format");
            return;
        }

        String type = parts[0];
        String name = parts[1];

        if (type.equals("artist")) {
            GraphNode node = artistTable.remove(name);
            if (node != null) {
                graph.removeNode(node);
                System.out.println("Artist " + name + " removed");
            }
            else {
                System.out.println("Artist " + name + " not found");
            }
        }
        else if (type.equals("song")) {
            GraphNode node = songTable.remove(name);
            if (node != null) {
                graph.removeNode(node);
                System.out.println("Song " + name + " removed");
            }
            else {
                System.out.println("Song " + name + " not found");
            }
        }
        else {
            System.out.println("Invalid remove type: " + type);
        }
    }


    private void processPrint(String args) {
        switch (args) {
            case "artist":
                artistTable.printContents("artist");
                break;
            case "song":
                songTable.printContents("song");
                break;
            case "graph":
                graph.analyzeComponents();
                break;
            default:
                System.out.println("Invalid print option: " + args);
        }
    }


    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(
                "Usage: java GraphProject <init-hash-size> <command-file>");
            return;
        }

        int initHashSize = Integer.parseInt(args[0]);
        String commandFile = args[1];

        GraphProject project = new GraphProject(initHashSize);

        try (BufferedReader br = new BufferedReader(new FileReader(
            commandFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                project.processCommand(line.trim());
            }
        }
        catch (IOException e) {
            System.out.println("Error reading command file: " + e.getMessage());
        }
    }
}
