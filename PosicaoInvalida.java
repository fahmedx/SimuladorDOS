
public class PosicaoInvalida extends RuntimeException{
	static final long serialVersionUID = 1L;
	
	PosicaoInvalida(){	}
	PosicaoInvalida(String msg){ super(msg);}
}
