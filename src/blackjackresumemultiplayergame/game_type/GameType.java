package blackjackresumemultiplayergame.game_type;


public abstract class GameType {
public abstract int calculate(int card1, int card2, int card3);
public abstract int evaluate (int card1, int card2, int card3);


    public static boolean sumIsLessThan21(int card1, int card2, int card3) {
        return card1 + card2 + card3 < 21;
    }

    public static boolean sumIsLessThan17(int card1, int card2, int card3) {
        return (card1 + card2 + card3) < 17;
    }

    public static boolean AJQKare10(int card1, int card2, int card3) {
        if (card1 > 10 || card1 == 10) {
            card1 = 10;
        }
        if (card2 > 10 || card2 == 10) {
            card2 = 10;
        }
        if (card3 > 10 || card3 == 10) {
            card3 = 10;
        }
        return (card1 + card2 + card3) < 21;
    }

    public static boolean JQKare10(int card1, int card2, int card3) {
        if (card1 > 10) {
            card1 = 10;
        }
        if (card2 > 10) {
            card2 = 10;
        }
        if (card3 > 10) {
            card3 = 10;
        }
        return (card1 + card2 + card3) < 21;
    }

    public static int sumvalueAJQKare10(int card1, int card2, int card3) {
        if (card1 > 10 || card1 == 10) {
            card1 = 10;
        }
        if (card2 > 10 || card2 == 10) {
            card2 = 10;
        }
        if (card3 > 10 || card3 == 10) {
            card3 = 10;
        }
        return (card1 + card2 + card3);
    }

    public static int sumvalueJQKare10(int card1, int card2, int card3) {
        if (card1 > 10) {
            card1 = 10;
        }
        if (card2 > 10) {
            card2 = 10;
        }
        if (card3 > 10) {
            card3 = 10;
        }
        return (card1 + card2 + card3);
    }

    public static int normalsum(int card1, int card2, int card3) {
        return (card1 + card2 + card3);
    }
}
