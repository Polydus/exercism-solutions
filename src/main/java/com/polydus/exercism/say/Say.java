package com.polydus.exercism.say;

public class Say {

    private final String[] names = {
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
            "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
    };

    private final String[] namesTen = {"", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

    private final String[] magnitudes = {"hundred", "thousand", "million", "billion"};
    private final StringBuilder sb = new StringBuilder();

    public String say(long number){
        if(number < 0 || number > 999_999_999_999L) throw new IllegalArgumentException();
        sb.setLength(0);

        if(number < 20){
            sb.append( names[(int) number]);
            return sb.toString();
        }

        long remainder = number;

        long billions = (remainder / 1_000_000_000);
        if(billions > 0){
            sb.append(genNumberString((int) billions));
            sb.append(" ");
            sb.append(magnitudes[3]);
            remainder -= billions * 1_000_000_000;
        }

        long millions = (remainder / 1_000_000);
        if(millions > 0){
            if(billions > 0) sb.append(" ");
            sb.append(genNumberString((int) millions));
            sb.append(" ");
            sb.append(magnitudes[2]);
            remainder -= millions * 1_000_000;
        }

        long thousands = (remainder / 1_000);
        if(thousands > 0){
            if(millions > 0) sb.append(" ");
            sb.append(genNumberString((int) thousands));
            sb.append(" ");
            sb.append(magnitudes[1]);
            remainder -= thousands * 1_000;
        }

        if(remainder > 0){
            if(thousands > 0) sb.append(" ");
            sb.append(genNumberString((int) remainder));
        }

        return sb.toString();
    }

    private String genNumberString(int number){
        int firstPlace = number % 10;
        int secondPlace = (number / 10) % 10;
        int thirdPlace = (number / 100) % 10;

        var sb = new StringBuilder();

        if(thirdPlace > 0){
            sb.append(names[thirdPlace]);
            sb.append(" ");
            sb.append(magnitudes[0]);
        }
        if(secondPlace > 0){
            if(thirdPlace == 0){
                sb.append(namesTen[secondPlace]);
            } else {
                sb.append(" ");
                sb.append(namesTen[secondPlace]);
            }
        }

        if(firstPlace > 0 || (secondPlace == 1)){
            if(secondPlace > 1){
                sb.append("-");
                sb.append(names[firstPlace]);
            } else {
                if(secondPlace != 1 && !(secondPlace == 0 & thirdPlace == 0)) sb.append(" ");

                sb.append(names[(firstPlace + secondPlace * 10)]);
            }
        }
        return sb.toString();
    }


}


