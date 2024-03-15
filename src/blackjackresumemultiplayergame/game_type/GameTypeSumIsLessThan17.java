/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjackresumemultiplayergame.game_type;

public class  GameTypeSumIsLessThan17 extends GameType{
    public GameTypeSumIsLessThan17(){
       System.out.println("You have selected the Win if sum is less than 17 condition");
    }

    @Override
    public int calculate (int card1,int card2, int card3)
    {
        return normalsum(card1, card2, card3);
    }
    @Override
    public int evaluate(int card1,int card2, int card3)
    {
        return sumIsLessThan17(card1,card2,card3)?1:0;
    }
} 
