package chapter1;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.io.*;
import javax.servlet.ServletContext;

@WebServlet(name = "MyFirstServlet", urlPatterns = {"/MyFirstServlet"})

public class MyFirstServlet extends HttpServlet {

    Tree myTree = new Tree();

    //-----------------------------------------------------------------------------------//
//									NODE CLASS
//-----------------------------------------------------------------------------------//
    public static class Node {

        Node left;
        Node right;
        double x;
        double y;
        String state;
        String city;

        public Node(String state, String city, double x, double y) {
            this.state = state;
            this.city = city;
            this.x = x;
            this.y = y;
            left = null;
            right = null;
        }
    }
//-----------------------------------------------------------------------------------//
//									TREE CLASS
//-----------------------------------------------------------------------------------//

    public static class Tree {

        public Node root;

        public Tree() {
            this.root = null;
        }

//-----------------------------------------------------------------------------------//
//							TREE - INSERTING A NODE IN THE TREE
//-----------------------------------------------------------------------------------//
        public void insertInTree(String state, String city, double x, double y) {
            Node temp = new Node(state, city, x, y);
            if (root == null) {
                root = temp;
                return;
            } else {
                Node current = root;
                int level = 0;
                while (true) {
                    if (level % 2 == 0) {
                        if (x <= current.x) {
                            if (current.left == null) {
                                current.left = temp;
                                return;
                            } else {
                                current = current.left;
                                level = level + 1;
                            }
                        } else {
                            if (current.right == null) {
                                current.right = temp;
                                return;
                            } else {
                                current = current.right;
                                level = level + 1;
                            }
                        }
                    } else {
                        if (y <= current.y) {
                            if (current.left == null) {
                                current.left = temp;
                                return;
                            } else {
                                current = current.left;
                                level = level + 1;
                            }
                        } else {
                            if (current.right == null) {
                                current.right = temp;
                                return;
                            } else {
                                current = current.right;
                                level = level + 1;
                            }
                        }
                    }
                }
            }
        }

//-----------------------------------------------------------------------------------//
//						TREE - DISPLAYING THE TREE
//-----------------------------------------------------------------------------------//
        public void display(Node root) {
            if (root != null) {
                display(root.left);
                System.out.println("City = " + root.city + ", x = " + root.x + ", y = " + root.y);
                display(root.right);
            }
        }
//-----------------------------------------------------------------------------------//
//						TREE - SEARCHING THE TREE
//-----------------------------------------------------------------------------------//

        public ArrayList<Node> search(Node temp, double x, double y, int counter, ArrayList<Node> array, double radius) {
            int level = 1;
            while (temp != null && counter > 0) {
                if (Math.abs(x - temp.x) <= radius && Math.abs(y - temp.y) <= radius) {
                    if (!array.contains(temp)) {
                        array.add(temp);
                        counter = counter - 1;
                    }
                    if (level % 2 == 0) {
                        if (x <= temp.x) {
                            temp = temp.left;
                        } else {
                            temp = temp.right;
                        }
                        level = level + 1;
                    } else {
                        if (y <= temp.y) {
                            temp = temp.left;
                        } else {
                            temp = temp.right;
                        }
                        level = level + 1;
                    }
                } else {
                    if (level % 2 == 0) {
                        if (x <= temp.x) {
                            temp = temp.left;
                        } else {
                            temp = temp.right;
                        }
                        level = level + 1;
                    } else {
                        if (y <= temp.y) {
                            temp = temp.left;
                        } else {
                            temp = temp.right;
                        }
                        level = level + 1;
                    }
                }
            }
            return array;
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        String[][] testarrayx = new String[12][12];
        for (int i = 0; i < 12; i++) {
            testarrayx[i][0] = "State";
            testarrayx[i][1] = "City";
            testarrayx[i][2] = "Latitude";
            testarrayx[i][3] = "Longitude";
        }
        String[] count = new String[5];
//        int flag1 = Integer.parseInt(request.getParameter("flag1"));
//        int flag2 = Integer.parseInt(request.getParameter("flag2"));
        int flag3 = Integer.parseInt(request.getParameter("flag3"));
        if (flag3 == 1) {
            makeTree();
        } else if (flag3 == 2) {
            if (myTree.root == null) {
                makeTree();
            }
            ArrayList<Node> arrayLeft = new ArrayList<Node>();
            ArrayList<Node> arrayRight = new ArrayList<Node>();
            ArrayList<Node> arrayFinal = new ArrayList<Node>();
            double x = 0.0;
            double y = 0.0;
            try {
                x = Double.parseDouble(request.getParameter("field1"));
                y = Double.parseDouble(request.getParameter("field2"));
            } catch (Exception e) {
            }
            double radius = 0.0;
            int counter = 10;

            while (arrayLeft.size() < 10) {
                radius = radius + 0.1;
                arrayLeft = myTree.search(myTree.root.left, x, y, counter, arrayLeft, radius);
                counter = 10 - arrayLeft.size();
            }

            counter = 10;
            radius = 0.0;

            while (arrayRight.size() < 10) {
                radius = radius + 0.1;
                arrayRight = myTree.search(myTree.root.right, x, y, counter, arrayRight, radius);
                counter = 10 - arrayRight.size();
            }
            int i = 0;
            int j = 0;
            while ((i < arrayLeft.size() && j < arrayRight.size()) || arrayFinal.size() < 1) {
                if ((Math.abs((arrayLeft.get(i).x) - x) < Math.abs(arrayRight.get(j).x) - x) && (Math.abs((arrayLeft.get(i).y) - y) < Math.abs(arrayRight.get(j).y) - y)) {
                    arrayFinal.add(arrayLeft.get(i));
                    i = i + 1;
                } else {
                    arrayFinal.add(arrayRight.get(j));
                    j = j + 1;
                }
            }
            testarrayx[0][0] = "State";
            testarrayx[0][1] = "City";
            testarrayx[0][2] = "Latitude";
            testarrayx[0][3] = "Longitude";
            testarrayx[11][0] = "none";
            testarrayx[11][1] = "none";
            double Doublee = 10000000;
            for (int m = 0; m < arrayFinal.size(); m++) {
                arrayFinal.get(m).x = Math.round(arrayFinal.get(m).x * 10000000) / Doublee;
                arrayFinal.get(m).y = Math.round(arrayFinal.get(m).y * 10000000) / Doublee;
            }
            testarrayx[11][2] = Double.toString(x);
            testarrayx[11][3] = Double.toString(y);
            for (int m = 0; m < arrayFinal.size(); m++) {
                testarrayx[m + 1][0] = arrayFinal.get(m).state;
                testarrayx[m + 1][1] = arrayFinal.get(m).city;
                testarrayx[m + 1][2] = Double.toString(arrayFinal.get(m).x);
                testarrayx[m + 1][3] = Double.toString(arrayFinal.get(m).y);
            }

        }
        else {
            double x1 = 0.0;
            double y1 = 0.0;
            double x2 = 0.0;
            double y2 = 0.0;
            try {
                x1 = Double.parseDouble(request.getParameter("field3"));
                y1 = Double.parseDouble(request.getParameter("field4"));
                x2 = Double.parseDouble(request.getParameter("field5"));
                y2 = Double.parseDouble(request.getParameter("field6"));
            } catch (Exception e) {
            }
                   String filename = "/WEB-INF/NationalFile_StateProvinceDecimalLatLong.txt";

        ServletContext context = getServletContext();
        InputStream is = context.getResourceAsStream(filename);
        int counter = 0;
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            PrintWriter writer = response.getWriter();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.matches(".*\\d+.*")) {
                    String str1 = (line.split("	"))[0];
                    String str2 = (line.split("	"))[1];
                    Double latitude = Double.parseDouble((line.split("	"))[2]);
                    Double longitude = Double.parseDouble((line.split("	"))[3]);
                    if (latitude < x1 && latitude >= x2 && longitude < y1 && longitude >= y2) {
                        counter = counter + 1;
                        System.out.println(line);
                    }
                }

            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error!");
        }
        System.out.println(counter);
            count[0] = Integer.toString(counter);
            count[1] = Double.toString(x1);
            count[2] = Double.toString(y1);
            count[3] = Double.toString(x2);
            count[4] = Double.toString(y2);
        }
        if (flag3 == 2) {
            PrintWriter outx = response.getWriter();
            outx.println(testarrayx);
            request.setAttribute("testarrayx", testarrayx);
            RequestDispatcher rdx = getServletContext().getRequestDispatcher("/MyFirstJSP.jsp");
            rdx.forward(request, response);
        } else {
            PrintWriter outx = response.getWriter();
            outx.println(count);
            request.setAttribute("count", count);
            RequestDispatcher rdx = getServletContext().getRequestDispatcher("/MyFirstJSP.jsp");
            rdx.forward(request, response);
        }
//        PrintWriter outx = response.getWriter();
//        outx.println(testarrayx);
//        request.setAttribute("testarrayx", testarrayx);
//        RequestDispatcher rdx = getServletContext().getRequestDispatcher("/MyFirstJSP.jsp");
//        rdx.forward(request, response);
    }

    public void makeTree() {
        String filename = "/WEB-INF/NationalFile_StateProvinceDecimalLatLong.txt";

        ServletContext context = getServletContext();
        InputStream is = context.getResourceAsStream(filename);
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
//            PrintWriter writer = response.getWriter();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.matches(".*\\d+.*")) {
                    myTree.insertInTree((line.split("	"))[0], (line.split("	"))[1], Double.parseDouble((line.split("	"))[2]), Double.parseDouble((line.split("	"))[3]));
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
