import ilog.concert.IloException;
import ilog.cplex.IloCplex;

public class SolveLP_WA {
    
    public SolveLP_WA(String file){
        try {
            IloCplex cplex=new IloCplex();
		    cplex.setParam(IloCplex.IntParam.MIPDisplay, 0);

            //解くlpファイル指定
			cplex.importModel(file); 
			//lpファイルを解く
			cplex.solve();	

            //目的関数の値を取得
			double objval = cplex.getObjValue();
			System.out.println("目的関数 :"+objval);
			cplex.end();
            
        } catch (IloException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }
}
