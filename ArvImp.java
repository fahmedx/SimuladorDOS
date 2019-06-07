import java.util.ArrayList;
import java.util.Iterator;

public class ArvImp<A> implements Arv<A> {
	
	private static class Op<A> extends Operacao<A>{
		String tostring = " ";
		public void pre(Posicao<A> p) {
			this.tostring += p.get() + " ";
		}
	}
	private static class No<A> implements Posicao<A>{
		ArrayList<No<A>> filhos;
		No<A> pai;
		A dados;
		Arv<A> cor;
		
		No(A t, Arv<A> newcolor){
			this.dados = t;
			this.cor = newcolor;
			this.filhos = new ArrayList<No<A>>();
		}
		
		public A get() {
			return this.dados;
		}
		public void put(A t) {
			this.dados = t;
		}
		public ArrayList<No<A>> getFilhos(){
			return this.filhos;
		}
	}
	private No<A> root;
	private ArrayList<No<A>> nos = new ArrayList<No<A>>();
	
	@Override
	public boolean empty() {
		return this.root == null;
	}
	
	@Override 
	public int tmn() {
		return this.nos.size();
	}
	
	@Override
	public boolean valid(Posicao<A> p) {
		try {
			this.convert(p);
		}catch(PosicaoInvalida i){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean hasParent(Posicao<A> p) throws PosicaoInvalida{
		if (this.isRoot(p)) {
			return false;
		}
		return true;
	}
	
	@Override 
	public boolean hasChildren(Posicao<A> p) throws PosicaoInvalida{
			No<A> n = this.convert(p);
			if(n.filhos.isEmpty()) {
				return false;
			}
			return true;
	}
	
	@Override
	public boolean isRoot(Posicao<A> p) throws PosicaoInvalida{
		if (this.convert(p) == this.root) {
			return true;
		}
		return false;
	}
	
	@Override
	public Posicao<A> inserirRaiz(A t) throws InsercaoE{
		if (this.root == null) {
			this.root = new No<A>(t, this);
			this.nos.add(this.root);
			return this.root();
		}
		throw new InsercaoE();
	}
	
	public Posicao<A> inserirFilho(Posicao<A> p, A t) throws PosicaoInvalida{
		No<A> n = this.convert(p);
		No<A> child = new No<A>(t,this);
		this.nos.add(child);
		n.filhos.add(child);
		child.pai = n;
		return child;
	}
	
	@Override
	public A removerEm(Posicao<A> p) throws PosicaoInvalida{
		No<A> n = this.convert(p);
		A d = n.dados;
		if (this.hasChildren(p)) {
			throw new RemocaoInvalida();
		}
		this.nos.remove(n);
		n.pai.filhos.remove(n);
		return d;
	}
	
	@Override
	public Posicao<A> root() throws ArvVaziaE{
		if (this.root == null) {
			throw new ArvVaziaE();
		}
		return this.root;
	}
	
	@Override
	public Posicao<A> pai(Posicao<A> p) throws PosicaoInvalida{
		return this.convert(p).pai;		
	}
	
	private No<A> convert(Posicao<A> p){
		No<A> n;
		if(p == null || !(p instanceof No<?>)) {
			throw new PosicaoInvalida();
		}
		n = (No<A>) p;
		if (n.cor != this) {
			throw new PosicaoInvalida();
		}
		return n;
	}
	@Override
	public Iterable<Posicao<A>> filho(Posicao<A> p) throws PosicaoInvalida{
		ArrayList<No<A>> children = this.convert(p).filhos;
		ArrayList<Posicao<A>> childpositions = new ArrayList<Posicao<A>>();
		for(No<A> n: children) {
			childpositions.add(n);
		}
		return childpositions;
	}
	
	@Override
	public Iterable<Posicao<A>> posicoes(){
		ArrayList<Posicao<A>> posicoes = new ArrayList<Posicao<A>>();
		for (No<A> n: this.nos) {
			Posicao<A> p = n;
			posicoes.add(p);
		}
		return posicoes;
	}
	
	@Override
	public Iterator<A> iterator(){
		ArrayList dados = new ArrayList<A>();
		for(No<A> n : this.nos) {
			dados.add(n.dados);
		}
		Iterator<A> itera = dados.iterator();
		return itera;
	}
	
	@Override
	public void traverse(Operacao<A> o) {
		this.recurse(this.root, o);
	}
	
	private void recurse(No<A> n, Operacao<A> op) {
		op.pre(n);
		if(this.hasChildren(n)) {
			for (int i=0; i<n.filhos.size()-1; i++) {
				this.recurse(n.filhos.get(i), op);
				op.in(n);
			}
			if(n.filhos.size() == 1) {
				op.in(n);
			}
			this.recurse(n.filhos.get(n.filhos.size() - 1), op);
		}else {
			op.in(n);
		}
		op.post(n);
	}
	
	public String toString() {
		String t = "[";
		
		Op<A> o = new Op<A>();
		this.traverse(o);
		t+="]";
		return t;
	}
	
}
