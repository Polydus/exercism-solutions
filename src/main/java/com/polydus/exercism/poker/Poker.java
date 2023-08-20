package com.polydus.exercism.poker;

import java.util.*;

public class Poker {

    ArrayList<Hand> hands = new ArrayList<Hand>();

    public Poker(List<String> hands) {
        for(String h: hands) this.hands.add(new Hand(h));
    }

    public List<String> getBestHands() {
        var sorted = hands.stream().sorted(Comparator.comparing(it -> it.currentHandType.ordinal())).toList();

        Hand.HandType bestHand = Hand.HandType.HIGH_CARD;
        for(int i = 0; i < sorted.size(); i++){
            if(sorted.get(i).currentHandType.ordinal() < bestHand.ordinal()) bestHand = sorted.get(i).currentHandType;
        }

        final Hand.HandType finalBestHand = bestHand;
        var allOfBestType = sorted.stream().filter(it -> it.currentHandType == finalBestHand).toList();

        if(allOfBestType.size() == 1) return allOfBestType.stream().map(it -> it.hand).toList();

        //return the best
        switch (bestHand){
            case ONE_PAIR -> {
                //find best pairs
                var highestPairCard = Hand.Card.Rank.RANK_2;

                for (Hand h : allOfBestType) {
                    var pair = h.bestPair(2);
                    if (pair.get(0).rank.ordinal() > highestPairCard.ordinal()) highestPairCard = pair.get(0).rank;
                }
                final var finalHighestPairCard = highestPairCard;
                //only keep the hands that have the best pair
                var remainingHands = allOfBestType.stream().filter(it -> it.bestPair(2).get(0).rank == finalHighestPairCard).toList();
                if (remainingHands.size() == 1) return remainingHands.stream().map(it -> it.hand).toList();


                //then, just rank by highest card

                //find the highest cards that aren't a part of the pair. Max 3 options
                for (var i = 0; i < 3; i++) {
                    final var finalIndex = i;

                    var bestCard = Hand.Card.Rank.RANK_2;
                    for(var j = 0; j < remainingHands.size(); j++){
                        var otherCards = Arrays.stream(remainingHands.get(j).
                                getCardsWithExceptions(finalHighestPairCard, 2))
                                .sorted(Comparator.comparing(it -> it.rank)).toList();

                        var highest = otherCards.get(otherCards.size() - 1 - finalIndex);
                        if(highest.rank.ordinal() > bestCard.ordinal()) bestCard = highest.rank;
                    }

                    final var finalBestCard = bestCard;
                    var handsWithBestCard = remainingHands.stream().filter(
                            it -> Arrays.stream(it.getCardsWithExceptions(finalHighestPairCard, 2))
                                    .anyMatch(c -> c.rank == finalBestCard)).toList();

                    if(handsWithBestCard.size() == 1) return handsWithBestCard.stream().map(it -> it.hand).toList();
                }
            }
            case TWO_PAIR ->  {
                //check if the two pairs are equal
                ArrayList<Hand> remainingHands = new ArrayList<>(allOfBestType);
                ArrayList<Hand> temp = new ArrayList<>();
                Hand.Card.Rank bestPairRank = Hand.Card.Rank.RANK_2;

                for(var i = 0; i < remainingHands.size(); i++){
                    var pair = remainingHands.get(i).bestPair(2);
                    if(pair.get(0).rank.ordinal() > bestPairRank.ordinal()) bestPairRank = pair.get(0).rank;
                }

                temp.clear();
                temp.addAll(remainingHands);
                remainingHands.clear();
                for(var i = 0; i < temp.size(); i++){
                    var pair = temp.get(i).bestPair(2);
                    if(pair.get(0).rank.ordinal() == bestPairRank.ordinal()) remainingHands.add(allOfBestType.get(i));
                }

                if(remainingHands.size() == 1) return remainingHands.stream().map(it -> it.hand).toList();

                //all best pairs are equal. So check the second pair
                Hand.Card.Rank secondBestPairRank = Hand.Card.Rank.RANK_2;
                for(var i = 0; i < remainingHands.size(); i++){
                    var pair = remainingHands.get(i).bestPair(2, 1);
                    if(pair.get(0).rank.ordinal() > secondBestPairRank.ordinal()) secondBestPairRank = pair.get(0).rank;
                }

                temp.clear();
                temp.addAll(remainingHands);
                remainingHands.clear();
                for(var i = 0; i < temp.size(); i++){
                    var pair = temp.get(i).bestPair(2, 1);
                    if(pair.get(0).rank.ordinal() == secondBestPairRank.ordinal()) remainingHands.add(allOfBestType.get(i));
                }

                if(remainingHands.size() == 1) return remainingHands.stream().map(it -> it.hand).toList();

                //check the last card for high card

                Hand.Card.Rank highCard = Hand.Card.Rank.RANK_2;
                for(var i = 0; i < remainingHands.size(); i++){
                    var cards = remainingHands.get(i).singleCards();
                    if(cards.get(0).rank.ordinal() > highCard.ordinal()) highCard = cards.get(0).rank;
                }

                temp.clear();
                temp.addAll(remainingHands);
                remainingHands.clear();
                for(var i = 0; i < temp.size(); i++){
                    var cards = temp.get(i).singleCards();
                    var thisHighCard = cards.get(0).rank;
                    if(highCard == thisHighCard) remainingHands.add(temp.get(i));
                }

                return remainingHands.stream().map(it -> it.hand).toList();

            }
            case THREE_OF_A_KIND -> {
                //find best pair
                var highestPairCard = Hand.Card.Rank.RANK_2;

                for (Hand h : allOfBestType) {
                    var pair = h.bestPair(3);
                    if (pair.get(0).rank.ordinal() > highestPairCard.ordinal()) highestPairCard = pair.get(0).rank;
                }
                final var finalHighestPairCard = highestPairCard;
                //only keep the hands that have the best pair
                var remainingHands = new ArrayList<Hand>(
                        allOfBestType.stream()
                                .filter(it -> it.bestPair(3).get(0).rank == finalHighestPairCard).toList()
                );
                if (remainingHands.size() == 1) return remainingHands.stream().map(it -> it.hand).toList();

                //then, just rank by highest card
                //find the highest cards that aren't a part of the pair. Max 2 options
                for (var i = 0; i < 2; i++) {
                    final var finalIndex = i;

                    var bestCard = Hand.Card.Rank.RANK_2;
                    for(var j = 0; j < remainingHands.size(); j++){
                        var otherCards = Arrays.stream(remainingHands.get(j).
                                        getCardsWithExceptions(finalHighestPairCard, 3))
                                .sorted(Comparator.comparing(it -> it.rank)).toList();

                        var highest = otherCards.get(otherCards.size() - 1 - finalIndex);
                        if(highest.rank.ordinal() > bestCard.ordinal()) bestCard = highest.rank;
                    }

                    final var finalBestCard = bestCard;
                    var handsWithBestCard = remainingHands.stream().filter(
                            it -> Arrays.stream(it.getCardsWithExceptions(finalHighestPairCard, 3))
                                    .anyMatch(c -> c.rank == finalBestCard)).toList();

                    if(handsWithBestCard.size() == 1) return handsWithBestCard.stream().map(it -> it.hand).toList();
                }
            }
            case FOUR_OF_A_KIND -> {
                //find best pair
                var highestPairCard = Hand.Card.Rank.RANK_2;

                for (Hand h : allOfBestType) {
                    var pair = h.bestPair(4);
                    if (pair.get(0).rank.ordinal() > highestPairCard.ordinal()) highestPairCard = pair.get(0).rank;
                }
                final var finalHighestPairCard = highestPairCard;
                //only keep the hands that have the best pair
                var remainingHands = new ArrayList<Hand>(
                        allOfBestType.stream()
                                .filter(it -> it.bestPair(4).get(0).rank == finalHighestPairCard).toList()
                );
                if (remainingHands.size() == 1) return remainingHands.stream().map(it -> it.hand).toList();

                //check the last card for high card

                Hand.Card.Rank highCard = Hand.Card.Rank.RANK_2;
                for(var i = 0; i < remainingHands.size(); i++){
                    var cards = remainingHands.get(i).singleCards();
                    if(cards.get(0).rank.ordinal() > highCard.ordinal()) highCard = cards.get(0).rank;
                }

                ArrayList<Hand> temp = new ArrayList<>();
                temp.clear();
                temp.addAll(remainingHands);
                remainingHands.clear();
                for(var i = 0; i < temp.size(); i++){
                    var cards = temp.get(i).singleCards();
                    var thisHighCard = cards.get(0).rank;
                    if(highCard == thisHighCard) remainingHands.add(temp.get(i));
                }

                return remainingHands.stream().map(it -> it.hand).toList();
            }

            case FULL_HOUSE ->  {
                //check if the two pairs are equal
                ArrayList<Hand> remainingHands = new ArrayList<>(allOfBestType);
                ArrayList<Hand> temp = new ArrayList<>();
                Hand.Card.Rank bestPairRank = Hand.Card.Rank.RANK_2;

                for(var i = 0; i < remainingHands.size(); i++){
                    var pair = remainingHands.get(i).bestPair(3);
                    if(pair.get(0).rank.ordinal() > bestPairRank.ordinal()) bestPairRank = pair.get(0).rank;
                }

                temp.clear();
                temp.addAll(remainingHands);
                remainingHands.clear();
                for(var i = 0; i < temp.size(); i++){
                    var pair = temp.get(i).bestPair(3);
                    if(pair.get(0).rank.ordinal() == bestPairRank.ordinal()) remainingHands.add(allOfBestType.get(i));
                }

                if(remainingHands.size() == 1) return remainingHands.stream().map(it -> it.hand).toList();

                //all best pairs are equal. So check the second pair
                Hand.Card.Rank secondBestPairRank = Hand.Card.Rank.RANK_2;
                for(var i = 0; i < remainingHands.size(); i++){
                    var pair = remainingHands.get(i).bestPair(2, 0, true);
                    if(pair.get(0).rank.ordinal() > secondBestPairRank.ordinal()) secondBestPairRank = pair.get(0).rank;
                }

                temp.clear();
                temp.addAll(remainingHands);
                remainingHands.clear();
                for(var i = 0; i < temp.size(); i++){
                    var pair = temp.get(i).bestPair(2, 0, true);
                    if(pair.get(0).rank.ordinal() == secondBestPairRank.ordinal()) remainingHands.add(allOfBestType.get(i));
                }

                return remainingHands.stream().map(it -> it.hand).toList();
            }
            case HIGH_CARD, FLUSH, STRAIGHT, STRAIGHT_FLUSH -> {

                ArrayList<Hand> remainingHands = new ArrayList<>(allOfBestType);

                if(bestHand == Hand.HandType.STRAIGHT || bestHand == Hand.HandType.STRAIGHT_FLUSH){
                    //exception for ace because a straight that starts with A is actually lower value
                    //so  [3, 4, 5, 6, A] beats [A, 2, 3, 4, 5]

                    Hand.Card.Rank[] ranks = {Hand.Card.Rank.RANK_A, Hand.Card.Rank.RANK_2};

                    var handsWithLowestStraight = remainingHands.stream()
                            .filter(it -> it.hasAllOfTheseRanks(ranks)).toList();

                    if(remainingHands.size() > handsWithLowestStraight.size()){
                        //have better straights than this, so continue with those
                        remainingHands.removeAll(handsWithLowestStraight);
                        if(remainingHands.size() == 1){
                            return remainingHands.stream().map(it -> it.hand).toList();
                        }
                    } else {
                        //return lowest straights
                        return handsWithLowestStraight.stream().map(it -> it.hand).toList();
                    }
                }

                //have to compare all cards, not just the highest one
                for(var i = 0; i < 5; i++){
                    final var finalIndex = i;

                    var sortedForHighestCard = remainingHands.stream().sorted(Comparator.comparing(it -> it.highestCard(finalIndex).getRankOrdinal())).toList();
                    var highestCard = sortedForHighestCard.get(sortedForHighestCard.size() - 1).highestCard(finalIndex);

                    var next = remainingHands.stream().filter(it -> it.highestCard(finalIndex).rank == highestCard.rank).toList();
                    if(next.size() == 1){
                        return next.stream().map(it -> it.hand).toList();
                    }
                    //continue on
                    remainingHands.clear();
                    remainingHands.addAll(next);
                }
                return remainingHands.stream().map(it -> it.hand).toList();
            }
        }
        return allOfBestType.stream().map(it -> it.hand).toList();
    }

    private class Hand{
        private Card[] cards;
        private String hand;
        private HandType currentHandType = HandType.HIGH_CARD;

        enum HandType{

            STRAIGHT_FLUSH,
            FOUR_OF_A_KIND,
            FULL_HOUSE,
            FLUSH,
            STRAIGHT,
            THREE_OF_A_KIND,
            TWO_PAIR,
            ONE_PAIR,
            HIGH_CARD,
            ;
        }

        public Hand(String hand) {
            this.hand = hand;
            var words = new ArrayList<String>();
            var lastIndex = 0;

            for(int i = 0; i < hand.length(); i++){
                if(hand.charAt(i) == ' '){
                    words.add(hand.substring(lastIndex, i));
                    lastIndex = i + 1;
                } else if(i == hand.length() - 1){
                    words.add(hand.substring(lastIndex, i + 1));
                }
            }

            cards = new Card[words.size()];

            for(var i = 0; i < words.size(); i++){
                var w = words.get(i);
                //find suit and value
                var suitStr = String.valueOf(w.charAt(w.length() - 1));

                Card.Suit suit = switch (suitStr){
                    case "S" -> Card.Suit.SPADES;
                    case "C" -> Card.Suit.CLUBS;
                    case "H" -> Card.Suit.HEARTS;
                    case "D" -> Card.Suit.DIAMONDS;
                    default -> Card.Suit.SPADES;
                };
                String rankStr;
                if(w.length() == 3){
                    rankStr = w.substring(0, 2);
                } else {
                    rankStr = w.substring(0, 1);
                }

                Card.Rank rank = switch (rankStr){
                    case "J" -> Card.Rank.RANK_J;
                    case "Q" -> Card.Rank.RANK_Q;
                    case "K" -> Card.Rank.RANK_K;
                    case "A" -> Card.Rank.RANK_A;
                    default -> Card.Rank.values()[Integer.parseInt(rankStr) - 2];
                };

                cards[i] = new Card(suit, rank);
            }

            calcCurrentHand();
        }

        void calcCurrentHand(){
            var cardsByRank = new HashMap<Card.Rank, ArrayList<Card>>();
            for(Card.Rank r: Card.Rank.values()) cardsByRank.put(r, new ArrayList<>());
            for(Card c: cards) cardsByRank.get(c.rank).add(c);

            //starts at the best one
            for(var i = 0; i < HandType.values().length; i++){
                var type = HandType.values()[i];

                switch (type){
                    case STRAIGHT_FLUSH -> {
                        if(isFlush() && isStraight()){
                            currentHandType = type;
                            return;
                        }
                    }
                    case FOUR_OF_A_KIND -> {
                        if(hasAnyPair(4, cardsByRank)){
                            currentHandType = type;
                            return;
                        }
                    }
                    case FULL_HOUSE -> {
                        var threeOfAKind = false;
                        for(var j = 0; j < Card.Rank.values().length; j++){
                            var rank = Card.Rank.values()[j];
                            if(!threeOfAKind){
                                if(cardsByRank.get(rank).size() == 3){
                                    threeOfAKind = true;
                                }
                            } else {
                                if(cardsByRank.get(rank).size() == 2){
                                    currentHandType = type;
                                    return;
                                }
                            }
                        }
                    }
                    case FLUSH ->  {
                        if(isFlush()){
                            currentHandType = type;
                            return;
                        }
                    }
                    case STRAIGHT ->  {
                        if(isStraight()){
                            currentHandType = type;
                            return;
                        }
                    }
                    case THREE_OF_A_KIND -> {
                        if(hasAnyPair(3, cardsByRank)){
                            currentHandType = type;
                            return;
                        }
                    }
                    case TWO_PAIR ->  {
                        if(hasPairs(2, 2, cardsByRank)){
                            currentHandType = type;
                            return;
                        }
                    }
                    case ONE_PAIR -> {
                        if(hasAnyPair(2, cardsByRank)){
                            currentHandType = type;
                            return;
                        }
                    }
                    case HIGH_CARD -> {
                        currentHandType = type;
                        return;
                    }
                }
            }
        }

        private boolean isStraight(){
            var singleCards = singleCards();
            if(singleCards.size() < 5) return false;
            var sortedByRank = Arrays.stream(cards).sorted(Comparator.comparingInt(Card::getRankOrdinal)).toList();

            if(sortedByRank.get(0).rank == Card.Rank.RANK_2
                    && sortedByRank.get(sortedByRank.size() - 1).rank == Card.Rank.RANK_A){
                //check for straight starting with a
                var found = true;
                for(var i = 1; i < sortedByRank.size() - 1; i++){
                    var delta = sortedByRank.get(i).getRankOrdinal() - sortedByRank.get(i - 1).getRankOrdinal();
                    if(delta != 1) found = false;
                }
                if(found) return true;
            }

            for(var i = 1; i < sortedByRank.size(); i++){
                var delta = sortedByRank.get(i).getRankOrdinal() - sortedByRank.get(i - 1).getRankOrdinal();
                if(delta != 1) return false;
            }
            return true;
        }
        private boolean isFlush(){
            var suit = cards[0].suit;
            return Arrays.stream(cards).allMatch(it -> it.suit == suit);
        }

        private boolean hasAnyPair(int pairSize, HashMap<Card.Rank, ArrayList<Card>> cardsByRank){
            return hasPairs(pairSize, 1, cardsByRank);
        }

        private boolean hasPairs(int pairSize, int pairAmount, HashMap<Card.Rank, ArrayList<Card>> cardsByRank){
            if(hand.length() < pairSize) return false;
            var pairsFound = 0;
            for(int i = Card.Rank.values().length - 1; i >= 0; i--){
                var rank = Card.Rank.values()[i];
                if(cardsByRank.get(rank).size() >= pairSize){
                    pairsFound++;
                    if(pairsFound == pairAmount) return true;
                }
            }
            return false;
        }
        List<Card> bestPair(int pairSize){
            return bestPair(pairSize, 0, false);
        }

        List<Card> bestPair(int pairSize, int pairOffset){
            return bestPair(pairSize, pairOffset, false);
        }

        List<Card> bestPair(int pairSize, int pairOffset, boolean pairSizeExact){
            var cardsByRank = new HashMap<Card.Rank, ArrayList<Card>>();
            for(Card.Rank r: Card.Rank.values()) cardsByRank.put(r, new ArrayList<>());
            for(Card c: cards) cardsByRank.get(c.rank).add(c);

            var offsetLeft = pairOffset;
            for(int i = Card.Rank.values().length - 1; i >= 0; i--){
                var rank = Card.Rank.values()[i];

                var valid = false;
                if(pairSizeExact){
                    valid = cardsByRank.get(rank).size() == pairSize;
                } else {
                    valid = cardsByRank.get(rank).size() >= pairSize;
                }

                if(valid){
                    //has a pair of this rank, return
                    if(offsetLeft > 0){
                        //don't return the best pair, return the N best pair
                        offsetLeft--;
                    } else {
                        return cardsByRank.get(rank).subList(0, pairSize);
                    }
                }
            }
            return Collections.emptyList();
        }

        List<Card> singleCards(){
            var cardsByRank = new HashMap<Card.Rank, ArrayList<Card>>();
            for(Card.Rank r: Card.Rank.values()) cardsByRank.put(r, new ArrayList<>());
            for(Card c: cards) cardsByRank.get(c.rank).add(c);

            var res = new ArrayList<Card>();

            for(int i = 0; i < Card.Rank.values().length; i++){
                var rank = Card.Rank.values()[i];
                if(cardsByRank.get(rank).size() == 1) res.addAll(cardsByRank.get(rank));
            }
            return res;
        }

        Card highestCard(){
            var list = Arrays.stream(cards).sorted(Comparator.comparingInt(Card::getRankOrdinal)).toList();
            return list.get(list.size() - 1);
        }

        Card highestCard(int offset){
            //assume we don't ask for an invalid index
            var list = Arrays.stream(cards).sorted(Comparator.comparingInt(Card::getRankOrdinal)).toList();
            return list.get(list.size() - 1 - offset);
        }

        Card highestCard(Card[] cards, int offset){
            //assume we don't ask for an invalid index
            var list = Arrays.stream(cards).sorted(Comparator.comparingInt(Card::getRankOrdinal)).toList();
            return list.get(list.size() - 1 - offset);
        }

        boolean hasAllOfTheseRanks(Card.Rank[] ranks){
            for(var i = 0; i < ranks.length; i++){
                final var iFinal = i;
                if(Arrays.stream(cards).noneMatch(it -> it.rank.ordinal() == ranks[iFinal].ordinal())) return false;
            }
            return true;
        }

        Card[] getCardsWithExceptions(Card.Rank exceptionRank, int amount){
            //assume we don't ask for an invalid index
            var list = new ArrayList<Card>();
            var dropped = 0;
            for(var i = 0; i < cards.length; i++){
                if(dropped < amount && cards[i].rank == exceptionRank){
                    dropped++;
                } else {
                    list.add(cards[i]);
                }
            }

            return list.toArray(Card[]::new);
        }

        class Card {

            enum Suit {
                HEARTS(Color.BLACK),
                DIAMONDS(Color.BLACK),
                CLUBS(Color.BLACK),
                SPADES(Color.BLACK),
                ;
                public final Color color;

                Suit(Color color) {
                    this.color = color;
                }
            }

            enum Color {
                RED,
                BLACK
            }

            enum Rank {
                RANK_2,
                RANK_3,
                RANK_4,
                RANK_5,
                RANK_6,
                RANK_7,
                RANK_8,
                RANK_9,
                RANK_10,
                RANK_J,
                RANK_Q,
                RANK_K,
                RANK_A,
                ;
            }

            Suit suit;
            Rank rank;

            public Card(Suit suit, Rank rank) {
                this.suit = suit;
                this.rank = rank;
            }

            public int getRankOrdinal() {
                return rank.ordinal();
            }

            @Override
            public String toString() {
                return "Card{" + "suit=" + suit + ", rank=" + rank + '}';
            }
        }
    }
}