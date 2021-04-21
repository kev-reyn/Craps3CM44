/**
 * 
 */
package craps;

/**
 * @author ItKevinR
 *
 * 07:52:05 PM
 */
import java.util.Random;

class Dado
{
	//variables
	private int valorDado_;
	public final static int MAX_VALOR_DADO = 6;
	public final static int MIN_VALOR_DADO = 1;
	private Random r;	
	
	//default constructor
	Dado()
	{
		valorDado_ = MIN_VALOR_DADO;
		r = new Random();
	}
	
	//metodos
	public void tirar()
	{
		valorDado_ = MIN_VALOR_DADO + r.nextInt(MAX_VALOR_DADO);
  	}
	
	public int getValor()
	{
		return valorDado_;
	}
	
	public String toString()
	{
		return "%d" + valorDado_;
	}
}
