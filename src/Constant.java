/*
 * 実験条件やデータパスなど不変的な情報を記述する静的クラス
 */
public class Constant {
		public static final int J = 3; //ジョブ数
		public static final int M = 3; //機械台数
		public static Condition jobCon = null; //ジョブ条件クラスのインスタンス
		public static WorkerCondition workCon = null; //作業者条件クラスのインスタンス

		public static String jobSetPath = "/Users/nagatadaiki/ExpData/JobData/3Machine/3J3M/set1/"; //ジョブセットのソースファイルパス
		public static String workerSetPath = "/Users/nagatadaiki/ExpData/WorkerSet/3Worker/Set1"; //作業者セットのソースファイルパス
		public static String expPath = "/Users/nagatadaiki/ExpData/3J3M_"; //実験ディレクトリのパス

}
