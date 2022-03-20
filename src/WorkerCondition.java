
/*
*作業者配置の条件クラス(これを基にLPファイル作成)
*/
public class WorkerCondition {

    private Worker[] workers;

    //作業者条件のコンストラクタ
    public WorkerCondition(){
        setWorkers();
    }

    //作業者をM人生成
    public void setWorkers(){
        workers = new Worker[Constant.M+1];
        for(int i=1;i<=Constant.M;i++){
            workers[i] = new Worker(i,Constant.workerSetPath);
        }
    }

    //作業者配列を取得
    public Worker[] getWorkers(){
        return this.workers;
    }

    //任意の作業者インスタンス取得
    public Worker getWorker(int i){
        return this.workers[i];
    }

    //任意の作業者の任意の機械に対するαの値を取得
    public double getAlpha(int i,int m){
        return this.workers[i].getAlpha()[m];
    }

    //任意の作業者の任意の機械に対するβの値を取得
    public double getBeta(int i,int m){
        return this.workers[i].getBeta()[m];
    }
    
}
