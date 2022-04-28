/*
 * 実験条件やデータパスなど不変的な情報を記述する静的クラス
 */
public class Constant {
		public static final int J = 9; //ジョブ数
		public static final int M = 9; //機械台数
		public static Condition jobCon = null; //ジョブ条件クラスのインスタンス
		public static WorkerCondition workCon = null; //作業者条件クラスのインスタンス

		public static String jobSetPath = "/Users/nagatadaiki/ExpData/JobData/9Machine/9J9M/set1/納期短"; //ジョブセットのソースファイルパス
		public static String workerSetPath = "/Users/nagatadaiki/ExpData/WorkerSet/9Worker/Set1"; //作業者セットのソースファイルパス
		public static String expPath = "/Users/nagatadaiki/ExpData/9J9M_納期短"; //実験ディレクトリのパス

}
