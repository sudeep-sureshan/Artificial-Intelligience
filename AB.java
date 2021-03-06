import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
public class AB
{
	String val_Max = new String();
	String val_Min = new String();
	String[] temp;
	String finalposition;
	
	public void alphabeta(grid g, int depth, Player player, Eval e, int size,Square grid_1[][],String I_Player) throws IOException
	{
		int number_of_moves=0;
		int x = 0,y=0;
//		if(player.getPlayer().equalsIgnoreCase("X"))
		{
//		  value_max(g,e,depth,number_of_moves,player,size,Integer.MIN_VALUE,Integer.MAX_VALUE,I_Player);
//		  temp= val_Max.split(" ");	
		}
//		else if(player.getPlayer().equalsIgnoreCase("O"))
		{
		  value_min(g,e,depth,number_of_moves,player,size,Integer.MIN_VALUE,Integer.MAX_VALUE,I_Player);
		  temp= val_Min.split(" ");
    	  finalposition = String.valueOf((char)(Integer.parseInt(temp[1])+65))+(Integer.parseInt(temp[0])+1);
    	  x=Integer.parseInt(temp[0]);
		  y=Integer.parseInt(temp[1]);
	   }
		if(temp[2].equalsIgnoreCase("Stake"))
		{
			grid_1[x][y].player=player.getPlayer();
		}
		else if(temp[2].equalsIgnoreCase("Raid"))
		{
			grid_1[x][y].player=player.getPlayer();
			if(g.isVaild(x, y-1, size)) //left
			{
				if(grid_1[x][y-1].player.equalsIgnoreCase(player.getOpponent().getPlayer())) // if opponent has occupied
				{
					grid_1[x][y-1].player=player.getPlayer();
				}
			}
			if(g.isVaild(x, y+1, size)) //right
			{
				if(grid_1[x][y+1].player.equalsIgnoreCase(player.getOpponent().getPlayer())) // if opponent has occupied
				{
					grid_1[x][y+1].player=player.getPlayer();
				}
			}
			if(g.isVaild(x+1, y, size)) //bottom
			{
				if(grid_1[x+1][y].player.equalsIgnoreCase(player.getOpponent().getPlayer())) // if opponent has occupie
				{
					grid_1[x+1][y].player=player.getPlayer();
				}
			}
			if(g.isVaild(x-1, y, size)) //top
			{
				if(grid_1[x-1][y].player.equalsIgnoreCase(player.getOpponent().getPlayer())) // if opponent has occupied
				{
					grid_1[x-1][y].player=player.getPlayer();
				}
			}
		}
		final PrintWriter bw=new PrintWriter("output.txt","UTF-8");
        bw.write(finalposition+" ");
        bw.write(temp[2]+"\n");
        for(int i=0;i<size;i++)
        {
        	for(int j=0;j< size;j++)
	        	{
        		  bw.write(String.valueOf(grid_1[i][j].player));
	        	}
        	bw.write("\n");
        }
        bw.close();

	}

	public int value_max(grid g, Eval e ,int depth,int number_of_moves, Player player,int size,int alpha,int beta,String I_Player)
	{
		if(number_of_moves == depth || (g.isFull(size)) )
		{
			return e.calc_gamescore(g, player.getPlayer(),size,I_Player);
		}
		else
		{  
			 MP obj=new MP();
			 ArrayList<String> hm = obj.moves_possible_stake(g,player.getPlayer(),size);
			  hm = obj.moves_possible_raid(hm,g,player.getPlayer(),size);
	         for(int i=0;i<hm.size();i++)
	           {
			       String q[]=hm.get(i).split(" ");
			       int x=Integer.parseInt(q[0]);
			       int y=Integer.parseInt(q[1]);
				grid sub_grid = g.clone(size);
				int value;
				sub_grid.make_move(x,y,q[2],player.getPlayer(),size);
				if(number_of_moves+1 == depth) // because u have made move but not updated in number_of_moves
				{
					value=e.calc_gamescore(sub_grid, player.getPlayer(),size,I_Player);
					if(alpha<value)
					{
						alpha=value;						
						val_Max=hm.get(i);
					}
				}
				else
				{
					value= value_min(sub_grid, e, depth,number_of_moves+1,player.getOpponent(),size,alpha,beta,I_Player);
					if(value > alpha)
					{
						alpha=value;
						val_Max=hm.get(i);
					}
				}
               if(alpha >= beta)
					{
						return alpha;
					}
			}
	    }
		return alpha;
	}

	public int value_min(grid g, Eval e ,int depth, int number_of_moves,Player player,int size,int alpha,int beta,String I_Player)
	{
		int beta1=Integer.MIN_VALUE;
		if(number_of_moves == depth || (g.isFull(size)) )
		{
			return e.calc_gamescore(g, player.getPlayer(),size,I_Player);
		}
		else
		{
			MP obj=new MP();
			ArrayList<String> hm = obj.moves_possible_stake(g,player.getPlayer(),size);
			hm = obj.moves_possible_raid(hm,g,player.getPlayer(),size);
		     for(int i=0;i<hm.size();i++)
		  {
           String q[]=hm.get(i).split(" ");
           int x=Integer.parseInt(q[0]);
           int y=Integer.parseInt(q[1]);
				grid sub_grid = g.clone(size);
				int value;
				sub_grid.make_move(x,y,q[2],player.getPlayer(),size);
				if(number_of_moves+1==depth) // because u have made move but not updated in number_of_moves
				{
					value=e.calc_gamescore(sub_grid, player.getPlayer(),size,I_Player);
					if(beta1 < value)
					{
						beta1=value;
						val_Min=hm.get(i);
					}
				}
				else
				{
					value = value_max(sub_grid, e, depth,number_of_moves+1,player.getOpponent(),size,alpha,beta,I_Player);
					if(beta> value)
					{
						beta=value;
						val_Min=hm.get(i);
					}
				}
				if(beta1!=Integer.MIN_VALUE)
				{
					if(beta1<=alpha)
					{
						return beta1;
					}
				}
				if(alpha>=beta)
				{
					return beta;
				}
			}
		}
		 return beta;
    }
}
