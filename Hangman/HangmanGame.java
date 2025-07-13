import java.util.Scanner;
class HangmanGame //The main game file which uses the necessary components from the GameLogic file
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        for(;;)
        {
            System.out.println("----------------"); //UI Print of the Game
            System.out.println("|   Hangman    |");
            System.out.println("|   Play (P)   |" );
            System.out.println("|   About (A)  |");
            System.out.println("|   Exit (E)   |");
            System.out.println("----------------"); 
            String choice = scanner.nextLine();
            if (choice.equals("P"))
            {
                System.out.println("Starting the game...\n");
                System.out.print("Enter the word that has to be guessed: ");
                String guess_word = scanner.nextLine();
                System.out.print("Enter the player's name who is going to guess the word: ");
                String player_name = scanner.nextLine();
                int failed_tries = 0;
                int tries = 0;
                int phase = 0;              //initilization of values before the guessing begins
                int player_score = 0;
                int char_reveals = 0;
                GameLogic gameHelper = new GameLogic();
                String[] wrong_characters = new String[12];
                String[] guessed_characters = new String[guess_word.length()];
                gameHelper.resetGuessedCharacters(guessed_characters,guess_word);
                String[][] hang = new String[7][5];
                boolean won = false;
                gameHelper.resetHang(hang);
                do
                {
                    tries++;
                    System.out.println("Current Hang Status: ");
                    gameHelper.drawHang(hang);
                                                                       
                    System.out.println("Current Word Status: "); //After every failed or not guess, shows hang and the progress of the guessing
                    gameHelper.viewGuessedCharacters(guessed_characters);

                    System.out.print(player_name+" Guess character, or word or reveal a random character which will cost you 3 moves of progress of the hang? (C for character or W for Word or R for reveal): ");
                    String player_choice = scanner.nextLine();

                    do
                    {
                        if(!player_choice.equals("C") && !player_choice.equals("W") && !player_choice.equals("R"))
                        {
                            System.out.print(player_name+" Please select a valid choice: ");                            //Ensuring valid data from player
                            player_choice = scanner.nextLine();
                        }
                    }while(!player_choice.equals("C") && !player_choice.equals("W") && !player_choice.equals("R"));
                    if(player_choice.equals("W"))
                    {
                        System.out.print(player_name+" Enter your word of choice: ");
                        String player_word = scanner.nextLine();
                        if( gameHelper.wordMatches(player_word,guess_word)) //player guesses word
                        {
                            won = true;
                            if (tries == 1)
                                player_score += 50;                             
                            else if(tries <=13)
                                player_score += (int) 50-((50*tries*7)/100);
                            else
                                player_score +=1;
                        }
                        else
                        {
                            System.out.println("Incorrect word!");
                            phase++;                                                    //player doesn't guess word
                            gameHelper.updateHang(hang,phase);  //updating the hang
                            failed_tries++;
                        }
                    }
                    else if(player_choice.equals("C"))
                    {
                        if(!gameHelper.isEmptyWrongCharactersList(wrong_characters))    //if list of wrong characters is empty no need to show it
                        {
                            System.out.print("Your previous wrong characters: ");
                            gameHelper.viewWrongCharactersList(wrong_characters);
                            System.out.print("\n");
                        }
                        System.out.print(player_name+" Enter a character: ");
                        String player_character = scanner.nextLine();
                        if(gameHelper.isEmptyWrongCharactersList(wrong_characters)) //if list is empty no need to check if the character is in the wrong char list
                        {
                            do
                            {    
                                if(player_character.length() > 1)
                                {
                                    System.out.print(player_name+" Please enter a character, not a string: ");
                                    player_character = scanner.nextLine();
                                }
                                else if( gameHelper.appearInGuessedCharacters(guessed_characters,player_character) > 0 )
                                {
                                    System.out.print(player_name+" Please enter a character that is not already in the word: ");
                                    player_character = scanner.nextLine();
                                }
                            } while(player_character.length() > 1 || gameHelper.appearInGuessedCharacters(guessed_characters,player_character) > 0 );
                        }
                        else
                        {
                            do
                            {    
                                if(player_character.length() > 1)
                                {
                                    System.out.print(player_name+" Please enter a character, not a string: ");  //checks if the player inserted a string and not a char
                                    player_character = scanner.nextLine();
                                }
                                else if(gameHelper.isInWrongCharactersList(wrong_characters,player_character))
                                {
                                    System.out.print(player_name+" Please enter a character which is not in the wrong character list: ");   //ensures that the player does not give a char which they said previously and is wrong
                                    player_character = scanner.nextLine();
                                }
                                else if( gameHelper.appearInGuessedCharacters(guessed_characters,player_character) > 0) //ensures that the player does not give an already guessed char
                                {
                                    System.out.print(player_name+" Please enter a character that is not already in the word: ");
                                    player_character = scanner.nextLine();
                                }
                            } while(gameHelper.isInWrongCharactersList(wrong_characters,player_character) || player_character.length() > 1 ||  gameHelper.appearInGuessedCharacters(guessed_characters,player_character) > 0);
                        }
                        if( gameHelper.isInWord(guess_word,player_character) )          //char is in the word
                        {
                            gameHelper.updateGuessedCharacters(guessed_characters,guess_word,player_character); //updates the progress
                            if(gameHelper.charsMatchWord(guess_word,guessed_characters)) //checks if player guesses the word by guessing the character that remains to be guessed
                            {                                                                                     
                                won = true;
                                player_score +=2;
                            }
                            else
                            {
                                System.out.println(player_name+" The character you gave is in the word!");
                                player_score +=2;
                            }
                        }
                        else
                        {
                            System.out.println(player_name+" The character you gave isn't in the word!");   //player doesn't guess a right character, gets added to the wrong chars list
                            phase++;
                            gameHelper.updateWrongCharactersList(wrong_characters,player_character);
                            gameHelper.updateHang(hang,phase);
                            failed_tries++;
                        }
                    }
                    else    //reveal system, progresses the hang by 3 phases
                    {
                        char_reveals++;
                        System.out.println("The character that has been revealed for you is: "+gameHelper.charReveal(guessed_characters,guess_word));
                        for(int i = 0; i<=2; i++)
                        {
                            phase++;
                            gameHelper.updateHang(hang,phase);
                        }
                        
                        if(gameHelper.charsMatchWord(guess_word,guessed_characters))    //checks if the player guesses the word by revealing the last character that remains to be guessed
                        {
                            player_score += 1;
                            won = true;
                            break;
                        }
                        else
                        {
                            player_score += 1;
                        }
                    }
                } while(!won && phase < 12);
                if(won)
                {
                    System.out.println(player_name+" Won the game with "+player_score+" score!");
                    System.out.println("------------------------------------------------");
                    System.out.println("               | Game Analysis |                ");     //Winning Screen
                    System.out.println(player_name+": Won the game.");
                    System.out.println("Score: "+player_score);
                    System.out.println("Total amount of tries: "+tries);
                    System.out.println("Total amount of failed tries: "+ failed_tries);
                    System.out.println("Total amount of character reveals: "+char_reveals);
                    System.out.println("Word to be guessed: "+guess_word);
                    System.out.println("-------------------------------------------------");
                }
                else 
                {
                    System.out.println("Current Hang Status: ");
                    gameHelper.drawHang(hang);
                    System.out.println("Current Word Status: "); 
                    gameHelper.viewGuessedCharacters(guessed_characters);
                    System.out.println("Sorry "+player_name+"! You lost, your score is: "+player_score);
                    System.out.println("------------------------------------------------");
                    System.out.println("               | Game Analysis |                ");
                    System.out.println(player_name+": Lost the game.");                                 //Losing screen
                    System.out.println("Score: "+player_score);
                    System.out.println("Total amount of tries: "+tries);
                    System.out.println("Total amount of failed tries: "+ failed_tries);
                    System.out.println("Total amount of word reveals: "+char_reveals);
                    System.out.println("Word to be guessed: "+guess_word);
                    System.out.println("-------------------------------------------------");
                }
            }
            else if(choice.equals("A"))
            {
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.println("\nThis game was made by Helio Zhuleku on 16/4/2025 with the Java Programming Language.\nThis game is made for 2 players only.\n");  //Credits
                System.out.println("--------------------------------------------------------------------------------------");
            }
            else if(choice.equals("E"))
            {
                System.out.println("Exiting the game...");  //Exit
                break;
            }
        }
    }
}