package com.example.saving_routes.algorithm;

import java.util.*;
import org.springframework.stereotype.Service;


@Service
public class Graph {
   /* private HashMap<String, Node> nodes;

    public Graph() {
        nodes = new HashMap<String, Node>();
    }

    public HashMap<String, Node> getNodes() {
        return nodes;
    }

    public void setNodes(HashMap<String, Node> nodes) {
        this.nodes = nodes;
    }

    public void addNode(Node newNode) {
        nodes.put(newNode.getId(), newNode);
    }

    public Node getNode(int index) {
        Node res = nodes.get(index);
        return res;
    }

    public Node setNode(int index) {
        Node res = nodes.get(index);
        return res;
    }*/

    private ArrayList<Node> nodes;

    public Graph() {
        nodes = new ArrayList<Node>();
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public void addNode(Node newNode) {
        nodes.add(newNode);
    }

    public Node getNode(int index) {
        Node res = nodes.get(index);
        return res;
    }

    public Node setNode(int index) {
        Node res = nodes.get(index);
        return res;
    }

    /*public void allNotVisited(Node startPoint) {
        for (Map.Entry<String, Node> node : nodes.entrySet()) {
            if (!node.getValue().getId().equals(startPoint.getId())) {
                node.getValue().setVisited(false);
            }
        }
    }*/

    public ArrayList<ArrayList<Integer>> getPermutations(int resSize){
        int counter = resSize;
        Integer[] firstpermutation = new Integer[counter];
        for(int i = 0; i< counter; i++){
            firstpermutation[i] = i + 1;
        }
        ArrayList<ArrayList<Integer>> permutations = new ArrayList<ArrayList<Integer>>();
        /*Permutations<Integer> perm = new Permutations<Integer>(firstpermutation);
        while(perm.hasNext()){
            Integer[] t = new Integer[counter];
            System.out.println(Arrays.toString(perm.next()));
            permutations.add(Arrays.toString(perm.next()));
        }*/
        permutations.addAll(permute(firstpermutation));
        return permutations;
    }

    /*public int shortWayPermute(Node startPoint,
                                Node endPoint,
                                ArrayList<Node> nodeArray,
                                Long curSum,
                                Long minSum,
                                int resSize
                                ){
        int counter = resSize;
        Integer[] firstpermutation = new Integer[counter];
        for(int i = 0; i< counter; i++){
            firstpermutation[i] = i + 1;
        }
        ArrayList<ArrayList<Integer>> permutations = new ArrayList<ArrayList<Integer>>();
        permutations.addAll(permute(firstpermutation));

        for (int i = 0; i < permutations.size(); i++) { 
            for (int j = 0; j < permutations.get(i).size(); j++) { 
                    if(haveEdge(startPoint, nodeArray.get(permutations.get(i).get(j))))
                    {
                        for(int f = j; f < permutations.get(i).size(); f++)
                        {
                            if(haveEdge(nodeArray.get(permutations.get(i).get(f)),  nodeArray.get(permutations.get(i).get(f+1))))
                            {

                            }
                        }
                        
                    }
                System.out.print(permutations.get(i).get(j) + " "); 
            } 
            System.out.println(); 
        } 
        int res = 1;
        return res;

    }*/

    public Long[] shortWayPermute(Node startPoint,
                                Node endPoint,
                                ArrayList<Node> nodeArray,
                                Long curSum,
                                int resSize
                                ){
                                    
        int counter = resSize;
        Integer[] firstpermutation = new Integer[counter];
        for(int i = 0; i< counter; i++){
            firstpermutation[i] = i;
        }
        ArrayList<ArrayList<Integer>> permutations = new ArrayList<ArrayList<Integer>>();
        int permuteArraySize = permutations.size();
        Long[] sums = new Long[permuteArraySize];

        permutations.addAll(permute(firstpermutation));

        for (int i = 0; i < permutations.size(); i++) { 
            for (int j = 0; j < permutations.get(i).size(); j++) { 
                curSum += getDuration(startPoint, nodeArray.get(permutations.get(i).get(j)));
                for(int f = 0; f < permutations.get(i).size() - 1; f++)
                {
                    curSum += getDuration(nodeArray.get(permutations.get(i).get(f)),  nodeArray.get(permutations.get(i).get(f+1)));
                    
                }
                curSum += getDuration(nodeArray.get(permutations.get(i).get(counter-1)), endPoint);
            } 
            sums[i] = curSum;
            curSum =  Long.valueOf(0);;

        } 
        return sums;

    }

    public Long getDuration(Node n1, Node n2){
        Long res = null;
        for (Edge edge : n1.getEdges()) {
            if(edge.getEndNode() == n2){
               res = edge.getDuration();
            }
        }
        return res;
    }

    public boolean haveEdge(Node n1, Node n2){
        boolean res = false;
        for (Edge edge : n1.getEdges()) {
            if(edge.getEndNode() == n2){
               return res = true;
            }
        }
        return res;
    }



    public ArrayList<ArrayList<Integer>> permute(Integer[] nums) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
        if (nums == null || nums.length == 0) {
            return results;
        }
        ArrayList<Integer> result = new ArrayList<>();
        dfs(nums, results, result);
        return results;
    }
    
    public void dfs(Integer[] nums, ArrayList<ArrayList<Integer>> results, ArrayList<Integer> result) {
        if (nums.length == result.size()) {
            ArrayList<Integer> temp = new ArrayList<>(result);
            results.add(temp);
        }        
        for (int i=0; i<nums.length; i++) {
            if (!result.contains(nums[i])) {
                result.add(nums[i]);
                dfs(nums, results, result);
                result.remove(result.size() - 1);
            }
        }
    }


//Near Neighbour
    public Long shortestWay(Node startPoint, Node endPoint, LinkedList<Node> resArray) {
        resArray.add(startPoint);
        if (startPoint.getEdges() == null) {
            return startPoint.getEdges().get(0).getDuration();
        }
        Long res = startPoint.getEdges().get(0).getDuration();
        int countPoint = nodes.size();
        int counter = 0;
        if (startPoint.getId().equals(endPoint.getId()) && counter == countPoint - 1) {
            return res;
        } else {
            for (Edge edge : startPoint.getEdges()) {
                if (!edge.getStartNode().isVisited()) {
                    startPoint.setVisited(true);
                    res += shortestWay(edge.getEndNode(), endPoint, resArray);
                    counter++;
                }
            }
        }
        return res;
    }
/* CUSTOM ALG
    public List<Edge> shortGamiltoneWay(Node startPoint,
                                  Node endPoint,
                                  LinkedList<Edge> resArray,
                                  LinkedList<Edge> minResArray,
                                  int counter,
                                  Long curSum,
                                  Long minSum, 
                                  int ind) { //ind roll back 
        Edge minEdge;
        if (counter == nodes.size() - 1) {
            allNotVisited(startPoint);
            minResArray.addAll(resArray);
            resArray.clear();
            return minResArray;
        }
        startPoint.setVisited(true);
        for (Edge edge : startPoint.getEdges()) {
            counter = resArray.size();
            if (edge.getEndNode().getId().equals(endPoint.getId()) && counter == nodes.size() - 2) {
                minEdge = edge;
                minEdge.getEndNode().setVisited(true);
                resArray.add(minEdge);
                curSum += minEdge.getDuration();
                if (curSum < minSum) {

                    shortGamiltoneWay(minEdge.getEndNode(), endPoint, resArray, resArray, counter, curSum += minEdge.getDuration(), curSum, ind);
                }
            } else if (edge.getEndNode().isVisited() == false && !edge.getEndNode().getId().equals(endPoint.getId())) {
                minEdge = edge;
                minEdge.getEndNode().setVisited(true);
                resArray.add(minEdge);
                curSum += minEdge.getDuration();
                if (curSum > minSum) {
                    break;
                }
                shortGamiltoneWay(minEdge.getEndNode(), endPoint, resArray, minResArray, counter, curSum, minSum, ind);
                //ind ++;
            }
        }return minResArray;

    }  */ 
}
