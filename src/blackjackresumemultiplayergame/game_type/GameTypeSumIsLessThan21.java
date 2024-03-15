/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjackresumemultiplayergame.game_type;

public class GameTypeSumIsLessThan21 extends GameType{
    public GameTypeSumIsLessThan21()
    {
        System.out.println("You have selected the Win if sum is less than 21");
    }
   
     @Override
    public int calculate (int card1,int card2, int card3)
    {
        return normalsum(card1, card2, card3);
    }
    @Override
    public int evaluate(int card1,int card2, int card3)
    {
        return sumIsLessThan21(card1,card2,card3)?1:0;
    }
    
}