public class All_Main {

	public static void main(String[] args) {
		
		int J=3;
		int M=3;
		
		//データセットを作成
		
		//実験データディレクトリパス
		String DataSetpath = "/Users/nagatadaiki/ExpData/Data2/Condition/";
		Condition condition = new Condition(J, M, DataSetpath);
		for(int j=1;j<=J;j++) {
			System.out.println(String.format("ジョブ%dの納期は%fです",j,condition.getDue()[j]));
		}
		
		MakeLP makeLP2 = new MakeLP(condition);
		//MakeLP makeLP = new MakeLP(condition);
		//Woker_MakeLP workerLP = new Woker_MakeLP(condition);
		
		
		
		
	}

}
