package JSPWA;
public class All_Main {

	public static void main(String[] args) {
		
		/*
		 * 実験開始
		 */
//		//実験クラスを作成
//		System.out.println("実験を開始します");
//		Experiment exp = new Experiment(); 
//		//実験に用いるジョブセットと作業者セットを実験フォルダにコピーしておく(記録用)
//		exp.copy(); 
//		//ログファイルにジョブセットパスと作業者セットパスを記録
//		LogFile.writeLog(Constant.expPath, "ジョブセットパス = "+Constant.jobSetPath);
//		LogFile.writeLog(Constant.expPath, "作業者セットパス = "+Constant.workerSetPath+"\n");
//		//作業者配置問題を求解
//		exp.makeLP_WA();
//		exp.solveLP_WA();
//		//ジョブショップスケジューリング問題を求解
//		exp.makeLP();
//		exp.solveLP();
//		//作業者ランダム配置実験
//		//exp.RandomWA();
//		System.out.println("実験を終了します");
		
		/*
		 * 実験終了
		 */
		for(int count=1;count<=1;count++) { //ジョブセットまたは作業者セットを変更して複数回試行するループ
			System.out.println("実験を開始します");
			//ジョブセット，作業者セット，実験結果ファイルのパスを指定
			String workerpath_common = Constant.workerSetPath;
			String jobsetpath_common = Constant.jobSetPath;
			String expPath_common = Constant.expPath;
			//Constant.workerSetPath = Constant.workerSetPath+count;
			//Constant.expPath = Constant.expPath+count;
//			switch(count) {
//			case 1:
//				Constant.jobSetPath = Constant.jobSetPath+"納期短";
//				Constant.expPath =Constant.expPath+"納期短";
//				break;
//			case 2:
//				Constant.jobSetPath = Constant.jobSetPath+"納期中";
//				Constant.expPath =Constant.expPath+"納期中";
//				break;
//			case 3:
//				Constant.jobSetPath = Constant.jobSetPath+"納期長";
//				Constant.expPath = Constant.expPath+"納期長";
//				break;
//			}
			//実験クラスを作成
			Experiment exp = new Experiment(); 
			//実験に用いるジョブセットと作業者セットを実験フォルダにコピーしておく(記録用)
			exp.copy(); 
			//ログファイルにジョブセットパスと作業者セットパスを記録
			LogFile.writeLog(Constant.expPath, "ジョブセットパス = "+Constant.jobSetPath);
			LogFile.writeLog(Constant.expPath, "作業者セットパス = "+Constant.workerSetPath+"\n");
			
			/*ジョブショップスケジューリング問題を作成*/
			exp.makeLP();
			
			/*求解*/
			exp.solveLP();
			
			System.out.println("実験を終了します");
			Constant.jobSetPath = jobsetpath_common;
			Constant.expPath = expPath_common;
			Constant.workerSetPath = workerpath_common;
		}
		
	}

}

