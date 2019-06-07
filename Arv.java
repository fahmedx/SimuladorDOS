import java.util.Iterator;
public interface Arv<A> extends Iterable<A>{
	boolean empty();
	int tmn();
	boolean valid(Posicao<A> p);
	
	boolean hasParent(Posicao<A> p) throws PosicaoInvalida;
	
	boolean hasChildren(Posicao<A> p) throws PosicaoInvalida;
	
	boolean isRoot(Posicao<A> p) throws PosicaoInvalida;
	
	Posicao<A> inserirRaiz(A t) throws InsercaoE;
	
	Posicao<A> inserirFilho(Posicao<A> p, A t) throws PosicaoInvalida;
	
	A removerEm(Posicao<A> p) throws PosicaoInvalida, RemocaoInvalida;
	
	Posicao<A> root() throws ArvVaziaE;
	
	Posicao<A> pai(Posicao<A> p) throws PosicaoInvalida;
	
	Iterable<Posicao<A>> filho(Posicao<A> p) throws PosicaoInvalida;
	
	Iterable<Posicao<A>> posicoes();
	
	Iterator<A> iterator();
	
	
	void traverse(Operacao<A> o);
	
}