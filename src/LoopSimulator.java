import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import MakeData.RandomNumber;

/*
 * 複数回シミュレーションを行うクラス
 * ・提案手法のロバストスケジュールを基に行う実際の生産活動をシミュレーションする．
 */
public class LoopSimulator {
	
	private double[][]ST; //計画段階でのジョブj工程kの処理開始時刻
	private double[][]CT; //計画段階でのジョブj工程kの処理完了時刻
	private double[][]ST_Re; //処理開始時刻の実現値
	private double[][]CT_Re; //処理完了時刻の実現値
	private double[][]Myu; //ジョブj工程kの作業時間平均μ
	private double[][]Sig; //ジョブj工程kの作業時間標準偏差σ
	//private double[][]PT_Re; //ジョブj工程kの作業時間の実現値を正規乱数から決定
	private ArrayList<Double[][]> PT_Re = new ArrayList<Double[][]>(); //各工程の標準作業時間
	private int LoopNum; //シミュレーション試行回数
	
	
	public LoopSimulator(int LoopNum) {
		this.ST = new double[Constant.J+1][Constant.M+1];
		this.CT = new double[Constant.J+1][Constant.M+1];
		this.ST_Re = new double[Constant.J+1][Constant.M+1];
		this.CT_Re = new double[Constant.J+1][Constant.M+1];
		this.Myu = new double[Constant.J+1][Constant.M+1];
		this.Sig = new double[Constant.J+1][Constant.M+1];
		this.set_Myu_Sig();
		this.set_RealPT_normal(LoopNum);
		//this.set_RealPT_erlang(LoopNum);
		this.LoopNum = LoopNum;
	}
	
	//ジョブj工程kの作業時間の実現値を正規乱数から決定
	public void set_RealPT_normal(int LoopNum) {
		for(int count=1;count<=LoopNum;count++) {
			Double[][] PT_temp = new Double[Constant.J+1][Constant.M+1];
			for(int j=1;j<=Constant.J;j++) {
				for(int k=1;k<=Constant.M;k++) {
					while(true){//正規分布(範囲制限)
						double rand_value = RandomNumber.createNormalRand(this.Myu[j][k],this.Sig[j][k]);
						if(this.Myu[j][k]-3*this.Sig[j][k]<=rand_value&&rand_value<=this.Myu[j][k]+3*this.Sig[j][k]) {
							PT_temp[j][k] = rand_value;
							break;
						}
					}
					//PT_temp[j][k] = RandomNumber.createNormalRand(this.Myu[j][k], this.Sig[j][k]);//正規分布(全範囲)
				}
			}
			this.PT_Re.add(PT_temp);
		}
	}
	
	//ジョブj工程kの作業時間の実現値を一様乱数から決定
	public void set_RealPT_uniform(int LoopNum) {
		for(int count=1;count<=LoopNum;count++) {
			Double[][] PT_temp = new Double[Constant.J+1][Constant.M+1];
			for(int j=1;j<=Constant.J;j++) {
				for(int k=1;k<=Constant.M;k++) {
					PT_temp[j][k] = RandomNumber.createUniformRand_Double(6*this.Sig[j][k], this.Myu[j][k]-3*this.Sig[j][k]);//一様分布
				}
			}
			this.PT_Re.add(PT_temp);
		}
	}
	
	//ジョブj工程kの作業時間の実現値をアーラン乱数から決定
	public void set_RealPT_erlang(int LoopNum) {
		for(int count=1;count<=LoopNum;count++) {
			Double[][] PT_temp = new Double[Constant.J+1][Constant.M+1];
			for(int j=1;j<=Constant.J;j++) {
				for(int k=1;k<=Constant.M;k++) {
					PT_temp[j][k] = RandomNumber.createErlangRand(2,2.0,Constant.jobCon.getPT(j, k));
				}
			}
			this.PT_Re.add(PT_temp);
		}
	}
	
	//作業時間平均μと標準偏差σをセット
	public void set_Myu_Sig() {
		BufferedReader br = null;
		for(int j=1;j<=Constant.J;j++) {
			try {
				//指定のファイルを読み込む
				String path = Constant.expPath+"/Job"+String.valueOf(j)+".csv";
				FileInputStream inputFile  = new FileInputStream(path);
				InputStreamReader isp = new InputStreamReader(inputFile);
				br = new BufferedReader(isp);
				String line;//csvファイルから読み込んだ一行分の文字列
				int row=0;//何行目か
				while((line = br.readLine())!=null){
					if(row>0) {
						String data[] = line.split(",");
						this.Myu[j][row]= Double.parseDouble(data[4]);
						this.Sig[j][row]= Double.parseDouble(data[5]);
					}
					row++;
				}
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				System.out.println("作業時間平均を読み込むためのファイルが見つかりませんでした");
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				System.out.println("作業時間平均のファイルの読み込みに失敗しました");
			}
			try {
				br.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}		
	}
	
	/*
	 * 以下スケジュールの頑健性及び効率性の評価指標を算出する関数
	 */
	
	//シミュレーション結果の総納期遅れを計算
	public double  Sim_tard() {
		Condition jobCon = Constant.jobCon;
		double[] Tj = new double[Constant.J+1]; 
		double sumTj = 0;
		for(int j=1;j<=Constant.J;j++) {
			Tj[j] = CT_Re[j][Constant.M] - jobCon.getDue()[j]; 
			if(Tj[j]<0) Tj[j] = 0;
			sumTj = sumTj + Tj[j];
		}
		return sumTj;
	}
	
	//スケジュール時点での総納期遅れと実際の生産活動での総納期遅れの差
	public double[] div_tard(double val_sche) {
		double[] results = new double[2];
		if(val_sche<this.Sim_tard()) { //実際の生産活動がスケジュールよりも遅れる場合
			results[0] = this.Sim_tard() - val_sche;
			results[1] = 0;
		}else { //実際の生産活動がスケジュールよりも早く終わる場合
			results[0] = 0;
			results[1] = val_sche - this.Sim_tard();
		}
		return results;
	}
	
	//スケジュールに対して実際の生産結果がどれだけオーバーしたかを算出
	public double over_CT() {
		double sum = 0;
		for(int j=1;j<=Constant.J;j++) {
			if(CT_Re[j][Constant.jobCon.getProcessNum()[j]]>CT[j][Constant.jobCon.getProcessNum()[j]]) {
				sum = sum + (CT_Re[j][Constant.jobCon.getProcessNum()[j]]-CT[j][Constant.jobCon.getProcessNum()[j]]);
			}
		}
		return sum;
	}
	
	//スケジュールに対して実際の生産結果がどれだけ短縮したか（スケジュールの余力が無駄になったか）を算出
	public double shortage_CT() {
		double sum = 0;
		for(int j=1;j<=Constant.J;j++) {
			if(CT_Re[j][Constant.jobCon.getProcessNum()[j]]<CT[j][Constant.jobCon.getProcessNum()[j]]) {
				sum = sum + (CT_Re[j][Constant.jobCon.getProcessNum()[j]]-CT[j][Constant.jobCon.getProcessNum()[j]]);
			}
		}
		return sum;
	}
	
	//スケジュールと実際の生産活動の完了時刻CTの絶対偏差
	public double div_CT() {
		double sum = 0;
		for(int j=1;j<=Constant.J;j++) {
			sum = sum + Math.abs(CT_Re[j][Constant.jobCon.getProcessNum()[j]]-CT[j][Constant.jobCon.getProcessNum()[j]]);
		}
		return sum;
	}
	
	//スケジュールのメイクスパン算出
	public double makespan_s() {
		double value = 0;
		for(int j=1;j<=Constant.J;j++) {
			if(value<this.CT[j][Constant.M])value=this.CT[j][Constant.M];
		}
		return value;
	}
	
	//シミュレーション結果のメイクスパン算出
	public double makespan_r() {
		double value = 0;
		for(int j=1;j<=Constant.J;j++) {
			if(value<this.CT_Re[j][Constant.M])value=this.CT_Re[j][Constant.M];
		}
		return value;	
	}
	
	//メイクスパンの差(スケジュール-シミュレーション)
	public double div_makespan() {
		return makespan_s()-makespan_r();
	}
	
	//生産活動のシミュレーション結果STを取得
	public double[][] getRealST(){
		return this.ST_Re;
	}
	
	//生産活動のシミュレーション結果CTを取得
	public double[][] getRealCT(){
		return this.CT_Re;
	}
	
	/*
	 *　実際の生産活動のシミュレートを複数回繰り返す
	 */
	 public void loopSimulation(double[][] ST,double[][] CT,double margin,double objval) {
		 double[] Sim_tard = new double[this.LoopNum+1];
		 double[] Sche_makespan = new double[this.LoopNum+1];
		 double[] Sim_makespan = new double[this.LoopNum+1];
		 double[] div_makespan = new double[this.LoopNum+1];
		 double[] Tard_plus = new double[this.LoopNum+1];
		 double[] Tard_minus = new double[this.LoopNum+1];
		 double[] over_CT = new double[this.LoopNum+1];
		 double[] shortage_CT = new double[this.LoopNum+1];
		 double[] div_CT = new double[this.LoopNum+1];
		 int count_div_tardiness = 0;
		 
		//実験ディレクトリを作成
		System.out.println("シミュレーション実験を開始");
		String expPath = Constant.expPath+"/Simulation"+String.valueOf(margin)+"σ"; //ディレクトリへのパス
		File jobSetFile = new File(expPath);
		if(jobSetFile.mkdir()) {
			System.out.println("シミュレーション実験ディレクトリの作成に成功しました");
		}
		
		//シミュレーション回す
		for(int count=1;count<=this.LoopNum;count++) {
			System.out.println("シミュレーション"+String.valueOf(count)+"回目のPT[1][1] = "+this.PT_Re.get(count-1)[1][1]);//確認用
			
			Simulation simulator = new Simulation(ST, CT,this.PT_Re.get(count-1));
			simulator.runSimulation();
			Output output = new Output(simulator.getRealST(),simulator.getRealCT(),null);
			output.MakeGunt(Constant.expPath+"/Simulation"+String.valueOf(margin)+"σ/"+String.valueOf(count)+"試行目.xml");
			Sim_tard[count] = simulator.Sim_tard();
			Sche_makespan[count] = simulator.makespan_s();
			Sim_makespan[count] = simulator.makespan_r();
			div_makespan[count] = simulator.div_makespan();
			Tard_plus[count] = simulator.div_tard(objval)[0];
			Tard_minus[count] = simulator.div_tard(objval)[1];
			over_CT[count] = simulator.over_CT();
			shortage_CT[count] = simulator.shortage_CT();
			div_CT[count] = simulator.div_CT();
		}
		try {
			FileWriter fw = new FileWriter(expPath+"/Simulation"+String.valueOf(margin)+"σ_"+Constant.J+"J"+Constant.M+"M.csv", false);
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			//ヘッダーの指定
           pw.print("Trial");//試行回数
           pw.print(",");
           pw.print("Schedule_Tard");//スケジュールの総納期遅れ
           pw.print(",");
           pw.print("Sim_Tard");//シミュレーション結果の総納期遅れ
           pw.print(",");
           pw.print("Tard_plus");//シミュレーション結果の総納期遅れースケジュールの総納期遅れ
           pw.print(",");
           pw.print("Tard_minus");//スケジュールの総納期遅れーシミュレーション結果の総納期遅れ
           pw.print(",");
           pw.print("makespan_s");//スケジュールのメイクスパン
           pw.print(",");
           pw.print("makespan_r");//シミュレーション結果のメイクスパン
           pw.print(",");
           pw.print("div_makespan");//メイクスパンの差(スケジュールのメイクスパンーシミュレーション結果のメイクスパン)
           pw.print(",");
           pw.print("over_CT");//シミュレーション結果のジョブの完了時刻の和-スケジュールのジョブの完了時刻の和
           pw.print(",");
           pw.print("shortage_CT");//シミュレーション結果のジョブの完了時刻の和-スケジュールのジョブの完了時刻の和
           pw.print(",");
           pw.print("div_CT");//完了時刻の絶対偏差
           pw.println();
	   		for(int count=1;count<=LoopNum;count++) {
	   			pw.print(count); 
	   			pw.print(",");
	            pw.print(objval);
	            pw.print(",");
	            pw.print(Sim_tard[count]);
	            pw.print(",");
	            pw.print(Tard_plus[count]);
	            if(Tard_plus[count]>0) count_div_tardiness++;
	            pw.print(",");
	            pw.print(Tard_minus[count]);
	            pw.print(",");
	            pw.print(Sche_makespan[count]);
	            pw.print(",");
	            pw.print(Sim_makespan[count]);
	            pw.print(",");
	            pw.print(div_makespan[count]);
	            pw.print(",");
	            pw.print(over_CT[count]);
	            pw.print(",");
	            pw.print(shortage_CT[count]);
	            pw.print(",");
	            pw.print(div_CT[count]);
	            pw.println();
	 		}
	   		pw.print(""+","+""+","+""+","+""+","+""+","+""+","+""+","+""+","+""+","+""+","+""+","+count_div_tardiness); //スケジュールを遵守できなかった回数をカウント
	   		pw.println();
	   		pw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	 }

}
