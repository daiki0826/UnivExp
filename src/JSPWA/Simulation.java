package JSPWA;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import MakeData.RandomNumber;

public class Simulation {
	
	private double[][]ST; //計画段階でのジョブj工程kの処理開始時刻
	private double[][]CT; //計画段階でのジョブj工程kの処理完了時刻
	private double[][]ST_Re; //処理開始時刻の実現値
	private double[][]CT_Re; //処理完了時刻の実現値
	private double[][]Myu; //ジョブj工程kの作業時間平均μ
	private double[][]Sig; //ジョブj工程kの作業時間標準偏差σ
	private double[][]PT_Re; //ジョブj工程kの作業時間の実現値
	
	public Simulation(double[][] ST,double[][] CT,Double[][] PT) {
		this.ST = new double[Constant.J+1][Constant.M+1];
		this.CT = new double[Constant.J+1][Constant.M+1];
		this.ST_Re = new double[Constant.J+1][Constant.M+1];
		this.CT_Re = new double[Constant.J+1][Constant.M+1];
		this.Myu = new double[Constant.J+1][Constant.M+1];
		this.Sig = new double[Constant.J+1][Constant.M+1];
		this.PT_Re = new double[Constant.J+1][Constant.M+1];
		this.setST(ST);
		this.setCT(CT);
		this.set_Myu_Sig();
		if(PT!=null) { //作業時間を意図的に設定したい時
			for(int j=1;j<=Constant.J;j++) {
				for(int k=1;k<=Constant.M;k++) {
					this.PT_Re[j][k] = PT[j][k];
				}
			}
		}else { //全てのシミュレーションにおいて作業時間を正規乱数で発生させる場合．
			this.set_RealPT();
		}
	}
	
	//生産計画段階での処理開始時刻をセット
	public void setST(double[][] ST) {
		this.ST = ST;
	}
	
	//生産計画段階での処理完了時刻をセット
	public void setCT(double[][] CT) {
		this.CT = CT;
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
	
	//ジョブj工程kの作業時間の実現値を正規乱数から決定
	public void set_RealPT() {
		for(int j=1;j<=Constant.J;j++) {
			for(int k=1;k<=Constant.M;k++) {
				this.PT_Re[j][k] = RandomNumber.createNormalRand(this.Myu[j][k], this.Sig[j][k]);
			}
		}
	}
	
	/*
	 * シミュレーションを実行
	 * 作業が予定より早く終わる場合は左詰，遅延する場合は後倒しにしたスケジュールを決定
	 */
	public void runSimulation() {
		/*（ジョブ番号,工程番号)をkey,予定処理開始時刻STをvalueとしたハッシュマップ
		 * keyが二次元の値を持つため，keyをさらにハッシュマップ(key:ジョブ番号,value:工程番号)とする．
		 * ハッシュマップの中にハッシュマップがある構造
		 */
		HashMap<HashMap<Integer, Integer>,Double> Priority = new HashMap<HashMap<Integer,Integer>,Double>(); 
		for(int j=1;j<=Constant.J;j++) {
			for(int k=1;k<=Constant.M;k++) {
				HashMap<Integer, Integer> map_j_k = new HashMap<Integer, Integer>(); //key:ジョブ番号,value:工程番号のハッシュマップ
				map_j_k.put(j, k);
				Priority.put(map_j_k,this.ST[j][k]);
			}
		}
		/*
		 * 機械毎のオーダ（ジョブ順序)を保存する二次元リスト(リストの各要素がリスト)
		 * リストの各要素は各機械のオーダ（ジョブ順序）をリストとして持つ．
		 */
		ArrayList<ArrayList<Double>> MachineList = new ArrayList<ArrayList<Double>>();
		for(int m=1;m<=Constant.M;m++) {
			ArrayList<Double> order_m = new ArrayList<Double>();
			MachineList.add(order_m);
		}
		
		//ハッシュマップをvalueであるSTの昇順にソートする．
		List<Entry<HashMap<Integer, Integer>, Double>> list = new ArrayList<>(Priority.entrySet());
		list.sort(Entry.comparingByValue());
		
		/*
		 * 実際の生産活動をシミュレートする
		 * 予定処理開始時刻STの小さい順に各工程の実現値PT_Reを基に実際の処理開始時刻ST_Reを決定する
		 */
		for(Entry<HashMap<Integer, Integer>,Double> entry : list) {
			Map< Integer, Integer> map = entry.getKey();
			for(Map.Entry<Integer,Integer> entry2 : map.entrySet()) {
				double ST = entry.getValue();
				int j = entry2.getKey();
				int k = entry2.getValue();
				int m = Constant.jobCon.getPM(j, k);
				//System.out.println(" ST = "+ST+", "+j+"-"+k);
				if(ST==0) {//予定処理開始時刻ST=0の場合はそのままST_Re＝0として決定
					this.ST_Re[j][k] = 0;
					this.CT_Re[j][k] = this.ST_Re[j][k] + this.PT_Re[j][k];
					MachineList.get(m-1).add(this.CT_Re[j][k]);
				}else {
					if(k!=1) { 
						/*
						 * 工程番号が1ではない場合は以下2点を考慮して処理開始時刻ST_Reを割当てる必要がある．
						 * ①同ジョブ前工程の処理完了時刻CT_Re以上にならなければならない
						 * ②同機械の先行ジョブのCT_Re以上にならなければならない．
						 */
						double ST_temp = this.CT_Re[j][k-1];
						double CT_temp = 0;
						for(double CT : MachineList.get(m-1)) {
							CT_temp = CT;
						}
						if(ST_temp<=CT_temp) {
							ST_temp = CT_temp;
						}
						this.ST_Re[j][k] = ST_temp;
						this.CT_Re[j][k] = this.ST_Re[j][k] + this.PT_Re[j][k];
						MachineList.get(m-1).add(this.CT_Re[j][k]);
					}else { //工程番号が1の場合は先行工程がないので上の制約②のみを保証すれば良い．
						double CT_temp = 0;
						for(double CT : MachineList.get(m-1)) {
							CT_temp = CT;
						}
						this.ST_Re[j][k] = CT_temp;
						this.CT_Re[j][k] = this.ST_Re[j][k] + this.PT_Re[j][k];
						MachineList.get(m-1).add(this.CT_Re[j][k]);
					}
				}
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
		
	//スケジュールに対してシミュレーション結果がどれだけオーバーしたかを算出
	public double over_CT() {
		double sum = 0;
		for(int j=1;j<=Constant.J;j++) {
			if(CT_Re[j][Constant.jobCon.getProcessNum()[j]]>CT[j][Constant.jobCon.getProcessNum()[j]]) {
				sum = sum + (CT_Re[j][Constant.jobCon.getProcessNum()[j]]-CT[j][Constant.jobCon.getProcessNum()[j]]);
			}
		}
		return sum;
	}
	
	//スケジュールに対してシミュレーション結果がどれだけ短縮したか（スケジュールの余力が無駄になったか）を算出
	public double shortage_CT() {
		double sum = 0;
		for(int j=1;j<=Constant.J;j++) {
			if(CT_Re[j][Constant.jobCon.getProcessNum()[j]]<CT[j][Constant.jobCon.getProcessNum()[j]]) {
				sum = sum + (CT_Re[j][Constant.jobCon.getProcessNum()[j]]-CT[j][Constant.jobCon.getProcessNum()[j]]);
			}
		}
		return sum;
	}
	
	//スケジュールとシミュレーション結果の完了時刻CTの絶対偏差
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

}
