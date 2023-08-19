package com.polydus.excercism.dominoes;

import java.util.ArrayList;
import java.util.List;

public class Dominoes {

    public List<Domino> formChain2(List<Domino> list) throws ChainNotFoundException{
        switch (list.size()){
            case 0 -> {return list;}
            case 1 -> {
                var d = list.get(0);
                if(d.getLeft() == d.getRight()) return list;
                throw new ChainNotFoundException("No domino chain found.");
            }
            default -> {
                //already sorted
                if(tryOrder(list)) return list;
            }
        }


        var listSize = list.size();

        var universe = new ArrayList<Domino>();
        universe.addAll(list);
        var current = new ArrayList<Domino>();

        for(int i = 0; i < listSize; i++) {
            current.clear();
            universe.clear();
            universe.addAll(list);

            //get from list, not universe bc theyre in order
            var firstStone = list.get(i);
            universe.remove(firstStone);
            current.add(firstStone);

            for(int j = 0; j < listSize; j++) {
                if(i == j) continue;
                //get from list, not universe bc theyre in order
                var lastStone = list.get(j);
                universe.remove(lastStone);

                if(firstStone.getLeft() != lastStone.getRight()){
                    //check if valid if you flip first stone
                    if(firstStone.getRight() == lastStone.getRight()){
                        //valid if first stone is flipped, so do that
                        firstStone = flipDomino(firstStone);
                    } else if(firstStone.getLeft() == lastStone.getLeft()){
                        //valid if last stone is flipped, so do that
                        lastStone = flipDomino(lastStone);
                    } else if(firstStone.getRight() == lastStone.getLeft()){
                        //valid if both stones are flipped, so do that
                        firstStone = flipDomino(firstStone);
                        lastStone = flipDomino(lastStone);
                    } else {
                        //not valid. continue
                        continue;
                    }
                }


                if(listSize == 2){
                    current.add(lastStone);
                    if(tryOrder(current)) return current;
                    continue;
                } else if(listSize == 3){
                    current.add(universe.get(0));
                    current.add(lastStone);
                    if(tryOrder(current)) return current;
                    current.set(1,flipDomino(current.get(1)));
                    if(tryOrder(current)) return current;
                    continue;
                }

                //first && last stone are valid. Let's check for the middle
                var midSize = listSize - 2;
                var currentMidArray = new ArrayList<Domino>();
                ArrayList<Domino>[] blackList = new ArrayList[midSize];


                for(var k = 0; k < midSize; k++){
                    var index = k;
                    Domino previous;
                    if(k == 0){
                        previous = firstStone;
                    } else {
                        previous = currentMidArray.get(k - 1);
                    }
                    //try a non flipped
                    var options = universe.stream()
                            .filter(it -> !blackList[index].contains(it))
                            .filter(it -> it.getLeft() == previous.getRight())
                            .toList();
                    if(options.isEmpty()){

                        break;
                    }
                    var next = options.get(0);
                    currentMidArray.add(next);
                }

                if(currentMidArray.size() == midSize){
                    current.addAll(currentMidArray);
                    current.add(lastStone);
                    if(tryOrder(current)) return current;
                    current.clear();
                    current.add(firstStone);
                }


            }
        }


        throw new ChainNotFoundException("No domino chain found.");










        /*
        ArrayList<Domino>[] blackList = new ArrayList[listSize];
        var current = new ArrayList<Domino>();
        var universe = new ArrayList<Domino>();
        universe.addAll(list);

        var running = true;

        while (running){
            current.clear();

            Domino previous;
            var options = universe.stream().filter(it -> !blackList[0].contains(it)).toList();
            if(options.isEmpty()){
                throw new ChainNotFoundException("No domino chain found.");
            }
            previous = options.get(0);
            current.add(previous);

            for(int i = 1; i < listSize; i++){
                Domino next;
                final int index = i; //ok then
                options = universe.stream()
                        .filter(it -> !blackList[index].contains(it)).toList();




                //var next = universe.stream().
            }

            if(current.size() == listSize && tryOrder(current)){
                return current;
            }
            running = false;
        }
*/

        /*


        var current = new ArrayList<Domino>();
        var remaining = new ArrayList<Domino>();

        for(int firstValue = 0; firstValue < 6; firstValue++){
            for(var secondValue = 0; secondValue < 6; secondValue++) {
                current.clear();
                remaining.clear();
                remaining.addAll(list);

                final int value1 = firstValue + 1;
                final int value2 = secondValue + 1;

                var validStones = remaining.stream().filter(stone -> isDominoOfThisValue(stone, value1, value2)).toList();
                if(validStones.isEmpty()) continue;

                //flip if needed
                var firstStone = flipIfNeeded(validStones.get(0), value1, value2);

                remaining.remove(firstStone);
                current.add(firstStone);

                //see if a last stone can exist
                validStones = remaining.stream().filter(stone -> isDominoOfThisValue(stone, value1, value2)).toList();
                if(validStones.isEmpty()) continue;

                var lastStone = flipIfNeeded(validStones.get(0), value1, value2);
                remaining.remove(lastStone);

                if(remaining.isEmpty()){
                    //exit with 2 size
                    if(firstStone.getRight() != lastStone.getLeft()) throw new ChainNotFoundException("No domino chain found.");
                    current.add(lastStone);
                    return current;
                }

                //check for middle stones
                var midSize = remaining.size();
                ArrayList<Domino>[] exclusions = new ArrayList[midSize];
                for(var i = 0; i < midSize; i++) exclusions[i] = new ArrayList<>();

                var mid = new ArrayList<Domino>();
                var optionsLeft = true;
                //find next
                while (optionsLeft){
                    mid.clear();
                    remaining.clear();
                    remaining.addAll(list);
                    remaining.remove(firstStone);
                    remaining.remove(lastStone);

                    for(var i = 0; i < midSize; i++){
                        Domino last;
                        if(i == 0){
                            last = firstStone;
                        } else {
                            last = mid.get(i - 1);
                        }

                        var options = remaining.stream().filter(stone -> !exclusions[mid.size()].contains(stone)).toList();
                        if(options.isEmpty()){
                            optionsLeft = false;
                            break;
                        }

                        for(Domino d: options){
                            if(d.getLeft() != last.getRight()){
                                exclusions[mid.size()].add(d);
                            }
                        }

                        var next = options.stream().filter(s -> s.getLeft() == last.getRight()).toList();
                        if(next.isEmpty()){
                            //no more options, meaning that the last domino won't work.
                            if(i == 0){
                                throw new ChainNotFoundException("No domino chain found.");
                            } else {
                                exclusions[i - 1].add(lastStone);
                            }
                            break;
                        }
                        var nextStone = next.get(0);
                        mid.add(nextStone);
                        remaining.remove(nextStone);
                    }

                    if(mid.size() == midSize){
                        //lets try
                        var res = new ArrayList<Domino>();
                        res.add(firstStone);
                        res.addAll(mid);
                        res.add(lastStone);
                        if(tryOrder(res)){
                            return res;
                        } else {
                            //last domino won't work.
                            exclusions[mid.size() - 1].add(mid.get(mid.size() - 1));
                        }
                    } else {
                        //last domino won't work.
                        if(!mid.isEmpty())exclusions[mid.size() - 1].add(mid.get(mid.size() - 1));
                    }
                }


            }
        }

*/


        /*

        var currentUniverse = new ArrayList<Domino>();
        var currentChain = new ArrayList<Domino>();
        var currentChainValues = new ArrayList<Integer>();

        //each domino can be 1-6 on either side

        //

        //try once with each type as a starting point
        for(int firstValue = 0; firstValue < 6; firstValue++){
            currentChain.clear();
            currentChainValues.clear();
            currentUniverse.clear();
            currentUniverse.addAll(list);

            //add a starting stone
            for(var secondValue = 0; secondValue < 6; secondValue++){
                //if this stone doesn't exist, continue
                final int value1 = firstValue + 1;
                final int value2 = secondValue + 1;

                var validStones = currentUniverse.stream().filter(stone -> isDominoOfThisValue(stone, value1, value2)).toList();
                if(validStones.isEmpty()) continue;

                var firstStone = validStones.get(0);
                //flip if needed
                if(firstStone.getLeft() == value2 && firstStone.getRight() == value1){
                    firstStone = flipDomino(firstStone);
                }

                currentUniverse.remove(firstStone);
                currentChain.add(firstStone);

                currentChainValues.add(firstStone.getRight());


                //try all different universes

                //populate orders
                var possibleOrders = new ArrayList<int[]>();
                for(var i = 0; i < currentUniverse.size(); i++){
                    //create all possibilities

                }


                //for(int[] universe: possibleUniverses){

                //}




                while (currentUniverse.size() > 0){
                    //try to make the rest of the chain here
                    //var lastStone = currentChain.get(currentChain.size() - 1);
                    var lastValue = currentChainValues.get(currentChainValues.size() - 1);

                    //need to find a valid stone that has lastValue
                    var nextStones = currentUniverse.stream().filter(stone -> dominoHasValue(stone, lastValue)).toList();
                    if(nextStones.isEmpty()){
                        //no valid stones left, so this is a dead end
                        //just break for now
                        currentChain.clear();
                        currentChainValues.clear();
                        currentUniverse.clear();
                        currentUniverse.addAll(list);

                        //need to check for: a situation where we try all different orders that are possible
                        //

                        break;
                    } else {
                        var next = nextStones.get(0);
                        currentUniverse.remove(next);
                        if(next.getLeft() == lastValue){
                            currentChainValues.add(next.getRight());
                        } else {
                            //need to flip it
                            next = flipDomino(next);
                            currentChainValues.add(next.getLeft());
                        }
                        currentChain.add(next);
                    }
                }

                if(currentUniverse.isEmpty()){
                    //all dominoes used.
                    //still, only valid if the first and last values match
                    //chain should always be at least size 2

                    if(currentChain.get(0).getLeft() == currentChain.get(currentChain.size() - 1).getRight()){
                        return currentChain;
                    }
                }
            }

        }

        throw new ChainNotFoundException("No domino chain found.");*/
    }

    public List<Domino> formChain(List<Domino> list) throws ChainNotFoundException{
        switch (list.size()){
            case 0 -> {return list;}
            case 1 -> {
                var d = list.get(0);
                if(d.getLeft() == d.getRight()) return list;
                throw new ChainNotFoundException("No domino chain found.");
            }
            default -> {
                //already sorted
                if(tryOrder(list)) return list;
            }
        }


        var currentList = new ArrayList<Domino>();
        var universe = new ArrayList<Domino>();

        var sb = new StringBuilder();
        for(var i = 0; i < list.size(); i++){
            sb.append("[" + list.get(i).getLeft() + "," + list.get(i).getRight() + "] ");
        }
        System.out.println(sb.toString());


        for(var x = 0; x < list.size(); x++){
            universe.clear();
            universe.addAll(list);

            currentList.clear();
            currentList.add(universe.get(x));
            universe.remove(currentList.get(0));

            for(var f = 0; f < 2; f++){
                if(f == 1) {
                    if(isMirrored(currentList.get(0))) continue;
                    //try again with flipped first stone
                    currentList.set(0, flipDomino(currentList.get(0)));
                }

                ArrayList<Domino>[] blackList = new ArrayList[list.size()];
                ArrayList<Domino>[] blackListReversed = new ArrayList[list.size()];
                for(var i = 0; i < blackList.length; i++){
                    blackList[i] = new ArrayList<>();
                    blackListReversed[i] = new ArrayList<>();
                }
                var optionsLeft = true;

                while (optionsLeft){
                    for(var i = 0; i < list.size() - 1; i++){
                        final var index = i; //o k
                        var last = currentList.get(currentList.size() - 1);
                        var next = getNextValidFrom(last, universe.stream()
                                .filter(it -> (
                                        (blackList[index].stream().noneMatch(it2 -> equalsNoMirror(it, it2)))
                                             //   && (blackListReversed[index].stream().noneMatch(it2 -> equalsNoMirror(it, it2)))
                                        )
                                ).toList(), false);
                        if(next != null){
                            currentList.add(next);
                            universe.remove(next);
                        } else {
                            //can't find a valid stone. See if flip works

                            next = getNextValidFrom(last, universe.stream()
                                    .filter(it -> (
                                            (blackListReversed[index].stream().noneMatch(it2 -> equalsNoMirror(it, it2)))
                                            )
                                    ).toList(), true);
                            if(next != null){
                                currentList.add(next);
                                universe.remove(next);
                            } else {
                                if(i > 0){
                                    blackList[i].add(last);
                                } else {
                                    optionsLeft = false;
                                }
                                break;
                            }
                        }
                    }

                    if(currentList.size() == list.size() && tryOrder(currentList)){
                        return currentList;
                    }

                    System.out.println("no options left. currentList: ");
                    sb.setLength(0);
                    for(var j = 0; j < currentList.size(); j++){
                        sb.append("[" + currentList.get(j).getLeft() + "," + currentList.get(j).getRight() + "] ");
                    }
                    System.out.println(sb.toString());

                    var first = currentList.get(0);
                    currentList.clear();
                    currentList.add(first);
                }
            }
        }


        throw new ChainNotFoundException("No domino chain found.");
    }


    public Domino getNextValidFrom(Domino previous, List<Domino> list, boolean flip){
        for(Domino d: list){
            if(flip){
                if(d.getLeft() == previous.getRight()) return d;
            } else {
                if(d.getRight() == previous.getRight()) return flipDomino(d);
            }
        }
        return null;
    }

    public boolean tryOrder(List<Domino> list){
        //assume no flipping
        if(list.isEmpty()) return true;
        if(list.size() == 1){
            var d = list.get(0);
            if(d.getLeft() == d.getRight()) return true;
        }
        if(list.get(0).getLeft() != list.get(list.size() - 1).getRight()) return false;

        for(var i = 1; i < list.size(); i++){
            if(list.get(i - 1).getRight() != list.get(i).getLeft()) return false;
        }

        return true;
    }

    boolean equalsNoMirror(Domino d1, Domino d2){
        if(d1.getLeft() == d2.getLeft() && d1.getRight() == d2.getRight()) return true;
        return false;
    }

    boolean isMirrored(Domino d){
        if(d.getLeft() == d.getRight()) return true;
        return false;
    }
    Domino flipDomino(Domino d){
        return new Domino(d.getRight(), d.getLeft());
    }

    Domino flipIfNeeded(Domino d, int left, int right){
        if(d.getLeft() == right && d.getRight() == left){
            return new Domino(left, right);
        }
        return d;
    }

    boolean dominoHasValue(Domino d, int value){
        return (d.getLeft() == value || d.getRight() == value);
    }
    boolean isDominoOfThisValue(Domino d, int left, int right){
        return (d.getLeft() == left && d.getRight() == right) || (d.getLeft() == right && d.getRight() == left);
    }

}