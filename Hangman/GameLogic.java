import java.util.concurrent.ThreadLocalRandom;
class GameLogic //file with all the components needed for the game to work
{
    public void drawHang(String hang[][])   //draws hang
    {
        for(int i=0; i<7; i++)
        {
            for(int j=0; j<5; j++)
            {
                if(j == 4)
                    System.out.print(hang[i][j]+"\n");
                else
                    System.out.print(hang[i][j]);
            }
        }
    }

    public void resetHang(String[][] hang)  //resets hang
    {
        for(int i = 0; i<7; i++)
        {
            hang[i][0] = "|";
            for(int j=1; j<5; j++)
            {
                hang[i][j] = " ";
            }
        }
    }
    public void updateHang(String[][] hang, int phase)  //updates hang
    {
        if (phase <=3)
        {
            hang[0][phase] = "-";                         
        }
        else if(phase <=8)
        {
            if (phase == 5)
                hang[phase-3][3] = "O";
            else
                hang[phase-3][3] = "|";
        }
        else if(phase<= 10)
        {
            if (phase%2 == 0)
                hang[3][4] = "\\";
            else
                hang[3][2] = "/";
        }
        else
        {
            if (phase%2 == 0)
                hang[6][2] = "/";
            else
                hang[6][4] = "\\";
        }    
    }
    public void resetGuessedCharacters(String[] guessed_characters,String guess_word)   //initiliazes the guessed characters array (the progress of the player)
    {
        for(int i = 0; i<guessed_characters.length; i++)
        {
            guessed_characters[i] = "_";
        }
        int rand_pos = ThreadLocalRandom.current().nextInt(0, guess_word.length());    // Word selection is random
        updateGuessedCharacters(guessed_characters, guess_word,Character.toString(guess_word.charAt(rand_pos)) );
    }
    public void updateGuessedCharacters(String[] guessed_characters, String guess_word, String player_character)    //updates the guessed characters array (the progress of the player)
    {
        for(int i= 0; i<guess_word.length(); i++)
        {
            if( player_character.charAt(0) == guess_word.charAt(i))
                guessed_characters[i] = player_character;
        }
    }
   public void viewGuessedCharacters(String[] guessed_characters)   //shows the guessed characters array(the progress of the player)
    {
        for( int i = 0; i < guessed_characters.length; i++ )
        {
            if ( i == guessed_characters.length - 1 )
                System.out.print(guessed_characters[i]+"\n");
            else
                System.out.print(guessed_characters[i]);  
        }
    }
    public boolean wordMatches( String player_word, String guess_word) //checks if the word that the player entered matches the word to be guessed
    {
        return player_word.equals(guess_word);
    }
    public boolean isEmptyWrongCharactersList(String[] wrong_characters)    //checks if the wrong character list is empty
    {
        return wrong_characters[0] == null;
    }
    public int findLastPos(String[] wrong_characters)   //finds the last position where the wrong character array has a value and not null
    {
        int last_pos = 0;
         for( int i = 0; i < wrong_characters.length; i++ )
        {
            if(wrong_characters[i] == null)
            {
                last_pos = i-1;
                break;
            }
        } 
        return last_pos;    
    }
    public void viewWrongCharactersList(String[] wrong_characters)  //views the wrong char list
    {
        int last_pos = findLastPos(wrong_characters);
        for(int i = 0; i <= last_pos-1; i++)
        {
            System.out.print(wrong_characters[i]+",");
        }
        System.out.print(wrong_characters[last_pos]);
    }
    public boolean isInWrongCharactersList(String[] wrong_characters, String player_character)  //checks if the character that the player entered was an incorrect character that they said previously
    {
        int last_pos = findLastPos(wrong_characters);
        for(int i = 0; i <= last_pos; i++)
        {
            if(wrong_characters[i].equals(player_character))
                return true;
        }
        return false;
    }
    public boolean isInWord(String guess_word, String player_character) //checks if the char is in the word
    {
        for(int i = 0; i<guess_word.length(); i++)
        {
            if(guess_word.charAt(i) == player_character.charAt(0))
                return true;   
        }
        return false;
    }
    public boolean charsMatchWord(String guess_word, String[] guessed_characters)   //checks if the last char that is needed for the word completion which either gets revealed or guessed completes the word correctly
    {
        String word = "";
        for(int i = 0; i < guessed_characters.length; i++)
        {
            word += guessed_characters[i];
        }
        return word.equals(guess_word);
        
    }
    public void updateWrongCharactersList(String[] wrong_characters, String player_character)   //adds any new wrong characters
    {   
        int last_pos = findLastPos(wrong_characters)+1;
        wrong_characters[last_pos] = player_character;
    }
    public int appearInGuessedCharacters(String[] guessed_characters, String player_character)  //calculates the number of times a word is in the guessed chars list
    {
        int appeareances = 0;
        for(int i = 0; i < guessed_characters.length; i++)
        {
            if(guessed_characters[i].equals(player_character))
                appeareances++;
        }
        return appeareances;
    }  

    public String charReveal(String[] guessed_characters, String guess_word)    //Char reveal, works randomly too
    {
        int rand_pos;
        do
        {
            rand_pos = ThreadLocalRandom.current().nextInt(0, guess_word.length());
        } while(appearInGuessedCharacters(guessed_characters,Character.toString(guess_word.charAt(rand_pos))) > 0); //ensure that the random char that is being generated is not already guessed or revealed 

        updateGuessedCharacters(guessed_characters,guess_word,Character.toString(guess_word.charAt(rand_pos)));
        return Character.toString(guess_word.charAt(rand_pos));
    }
}