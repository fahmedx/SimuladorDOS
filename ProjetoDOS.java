import java.util.InputMismatchException;
import java.util.Scanner;

public class ProjetoDOS {
	private static class entrada{
		String nome;
	}
	private static class Pasta extends entrada{
		public Pasta(String initialName) {
			this.nome = initialName;
		}
	}
	
	private static class arquivo extends entrada{
		public arquivo(String initialName) {
			this.nome = initialName;
		}
	}
	public static Arv<entrada> sistema = new ArvImp<entrada>();
	public static Posicao<entrada> cwd;
	private ProjetoDOS() {}
	
	public static void main(String[] args) {
		Pasta root = new Pasta("/");
		cwd = sistema.inserirRaiz(root);
		Scanner cm = new Scanner(System.in);
		
		System.out.print(">");
		String cmd = "";
		String argm = "";
		while (cm.hasNext()) {
			try {
				cmd = cm.nextLine();
			}catch (InputMismatchException i) {
				System.out.println("Comando não encontrado!");
			}
			if (cmd.indexOf(" ")!= -1) {
				int index = cmd.indexOf(" ");
				argm = cmd.substring(index + 1, cmd.length());
				cmd = cmd.substring(0, index);
				process(cmd,argm);
			}else {
				process(cmd);
			}
			System.out.print("> ");
		}
	}
	public static void process(String cmdd) {
			switch (cmdd) {
			case "ls":
				System.out.print(lista(cwd));
				break;
			case "pwd":
				System.out.println(getCaminho(cwd));
				break;
			case "quit":
				System.exit(0);
				break;
			default:
				System.out.println("Comando Inválido!");
				break;
	     }
	}
		
	public static void process(String cmdd, String arg) {
			switch (cmdd) {
            case "cd":
                MudaPas(arg);
                break;
            case "mkdir":
                FazPas(arg);
                break;
            case "rmdir":
                DelPas(arg);
                break;
            case "mk":
                Faz(arg);
                break;
            case "rm":
                Del(arg);
                break;
            default:
                System.out.println("Comando Inválido!");
                break;
			}
	}
		
	public static String lista(Posicao<entrada> pas) {
			String lista ="";
			String[] nomes = sortAlf(pas);
			for (int i=0; i<nomes.length; i++) {
				lista+= nomes[i] +"\n";
			}
			return lista;
	}
	
	private static String[] sortAlf(Posicao<entrada> pas) {
		int ct = 0;
		for (Posicao<entrada> e : sistema.filho(pas)) {
			ct += 1;
		}
		String[] safira = new String[ct];
		
		int index = 0;
		for (Posicao<entrada> e : sistema.filho(pas)) {
			if (e.get() instanceof Pasta) {
				safira[index] = e.get().nome+"/";
			}else {
				safira[index] = e.get().nome;
			}
			index += 1;
		}
		for (int i = ct - 1; i>0; i--) {
			for (int j = 0; j<i; j++) {
				if(safira[j].compareToIgnoreCase(safira[j+1])>0) {
					String aux = safira[j];
					safira[j] = safira[j+1];
					safira[j+1]= aux;
				}
			}
		}
		return safira;
	}
	public static String getCaminho(Posicao<entrada> atual) {
		String cam = "/";
		if(sistema.isRoot(cwd)) {
			cam="/";
		}
		Posicao<entrada> cp = atual;
		
		while (sistema.hasParent(cp)) {
			cam = "/" + cp.get().nome + cam;
			cp = sistema.pai(cp);
		}
		return cam;
	}
	public static void MudaPas(String NovaPas) {
		boolean exists = false;
		if(NovaPas.equals("..")) {
			if(!sistema.isRoot(cwd)) {
				cwd = sistema.pai(cwd);
			}
		} else if (find(NovaPas)== null) {
			System.out.println("Pasta não encontrada!!");
		} else if(find(NovaPas).get() instanceof arquivo) {
			System.out.println("O item descrito não é uma pasta!");
		} else {
			cwd = find(NovaPas);
		}
	}
	
	public static void FazPas(String NovaPas) {
		entrada novaPasta = new Pasta(NovaPas);
		if (find(NovaPas)==null) {
			if (NovaPas.equals("..")) {
				System.out.println("Não é permitido nomear uma pasta de '/../''");
			}else {
				sistema.inserirFilho(cwd, novaPasta);
			}
		}else if(find(NovaPas).get() instanceof Pasta) {
			System.out.println("Ja existe uma pasta com este nome!");
		}else if (find(NovaPas).get() instanceof arquivo) {
			System.out.println("Ja existe um arquivo com este nome!");
		}
	}
	
	public static void DelPas(String pas) {
		if(find(pas)==null) {
			System.out.println("A pasta não existe");
		}else if (find(pas).get() instanceof arquivo) {
			System.out.println("Para remover arquivos, usa-se o comando 'rm'");
		} else {
			try {
				sistema.removerEm(find(pas));
			}catch(RemocaoInvalida fail) {
				System.out.println("A pasta não está vazia...");
			}
		}
	}
	
	public static void Faz(String Nv) {
		entrada na = new arquivo(Nv);
		if(find(Nv) == null) {
			if(Nv.equals("..")) {
				System.out.println("Não se pode nomear um arquivo como '/../'");
			}else {
				sistema.inserirFilho(cwd, na);
			}
		}else if (find(Nv) instanceof Pasta) {
			System.out.println("Ja existe uma pasta com esse nome!");
		}else if (find(Nv) instanceof arquivo) {
			System.out.println("Ja existe um arquivo com esse nome!");
		}
	}
	
	public static void Del(String arquivo) {
		if (find(arquivo)== null) {
			System.out.println("O arquivo não existe!!");
		}else if (find(arquivo).get() instanceof Pasta) {
			System.out.println("Para remover pastas, use rmdir!");
		}else {
			sistema.removerEm(find(arquivo));
		}
	}
	
	private static Posicao<entrada> find(String n){
		for (Posicao<entrada> e: sistema.filho(cwd)) {
			if (e.get().nome.equals(n)) {
				return e;
			}
		}
		return null;
	}
}
