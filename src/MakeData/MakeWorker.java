package MakeData;

import java.util.Random;

/*
* 作業者生成クラス
*/

public class MakeWorker {

    private int workerNum;
    private double[] alpha;
    private double[] beta;
    private int skill_level;
    private int M;
    
    //コンストラクタ(作業者のインスタンス作成)
    public MakeWorker(int i,int M,int skill_level){
        this.workerNum = i;
        this.M = M;
        this.skill_level = skill_level;
        //this.setSkill_level();
        this.setWorkerParams();
//        this.setAlpha();
//        this.setBeta();
    }

    //作業者の熟練度決定
    public void setSkill_level(){
        int value = RandomNumber.createUniformRand(3, 1);
        this.skill_level = value;
        
    }
    
    public void setWorkerParams() {
    	Random rand = new Random();
    	this.alpha = new double[this.M+1];
    	this.beta = new double[this.M+1];
    	switch(skill_level){
        case 1:
            for(int k=1;k<=this.M;k++){
            	double val1 = ((double)RandomNumber.createUniformRand(5,5))/10;
                double val2 = rand.nextDouble()/10;
                double val3 = ((double)Math.round((val1+val2) * 100))/100;
//            	this.alpha[k] = val3;
//            	this.beta[k] = val3;
                this.alpha[k] = 0.5;
            	this.beta[k] = 0.5;
            }
            break;
        case 2:
            for(int k=1;k<=this.M;k++){
            	this.alpha[k] = 1.0;
            	this.beta[k] = 1.0;
            }
            break;
        case 3:
            for(int k=1;k<=this.M;k++){
            	double val1 = ((double)RandomNumber.createUniformRand(5,10))/10;
                double val2 = rand.nextDouble()/10;
                double val3 = ((double)Math.round((val1+val2) * 100))/100;
//            	this.alpha[k] = val3;
//            	this.beta[k] = val3;
                this.alpha[k] = 1.5;
            	this.beta[k] = 1.5;
            }
            break;
    }
    }

    //作業時間平均を決定するパラメータαの決定
    //熟練度の区分によって乱数の範囲を設定
    //上級者(0.５<=α<=1.0),中級者(0.8<=α<=1.2),初級者(1.0<=α<=1.4)
    public void setAlpha(){
    	Random rand = new Random();
        this.alpha = new double[this.M+1];
        switch(skill_level){
            case 1:
                for(int k=1;k<=this.M;k++){
                    //0.5~0.99の一様乱数を生成
                    double val1 = ((double)RandomNumber.createUniformRand(5,5))/10;
                    double val2 = rand.nextDouble()/10;
                    double val3 = ((double)Math.round((val1+val2) * 100))/100;
                    this.alpha[k] = val3;
                }
                break;
            case 2:
                for(int k=1;k<=this.M;k++){
                    //0.8~1.29の一様乱数を生成
                	double val1 = ((double)RandomNumber.createUniformRand(5,8))/10;
                    double val2 = rand.nextDouble()/10;
                    double val3 = ((double)Math.round((val1+val2) * 100))/100;
                    this.alpha[k] = val3;
                }
                break;
            case 3:
                for(int k=1;k<=this.M;k++){
                    //1.0~1.49の一様乱数を生成
                	double val1 = ((double)RandomNumber.createUniformRand(5,10))/10;
                    double val2 = rand.nextDouble()/10;
                    double val3 = ((double)Math.round((val1+val2) * 100))/100;
                    this.alpha[k] = val3;
                }
                break;
        }
    }

    //作業時間標準偏差を決定するパラメータβの決定
    //熟練度の区分によって乱数の範囲を設定
    //上級者(0.6<=α<=1.0),中級者(0.8<=α<=1.2),初級者(1.0<=α<=1.4)
    public void setBeta(){
    	Random rand = new Random();
        this.beta = new double[this.M+1];
        switch(skill_level){
            case 1:
                for(int k=1;k<=this.M;k++){
                    //0.5~0.99の一様乱数を生成
                	double val1 = ((double)RandomNumber.createUniformRand(5,5))/10;
                    double val2 = rand.nextDouble()/10;
                    double val3 = ((double)Math.round((val1+val2) * 100))/100;
                    this.beta[k] = val3;
                }
                break;
            case 2:
                for(int k=1;k<=this.M;k++){
                    //0.8~1.29の一様乱数を生成
                	double val1 = ((double)RandomNumber.createUniformRand(5,8))/10;
                    double val2 = rand.nextDouble()/10;
                    double val3 = ((double)Math.round((val1+val2) * 100))/100;
                    this.beta[k] = val3;
                }
                break;
            case 3:
                for(int k=1;k<=this.M;k++){
                    //1.0~1.49の一様乱数を生成
                	double val1 = ((double)RandomNumber.createUniformRand(5,10))/10;
                    double val2 = rand.nextDouble()/10;
                    double val3 = ((double)Math.round((val1+val2) * 100))/100;
                    this.beta[k] = val3;
                }
                break;
        }
    }

    //作業者番号を取得
    public int getWokerNum(){
        return this.workerNum;
    }
    //熟練度を取得
    public int getSkill_level(){
        return this.skill_level;
    }

    //各機械に対するαを取得
    public double[] getAlpha(){
        return this.alpha;
    }

    //各機械に対するβを取得
    public double[] getBeta(){
        return this.beta;
    }
    


}
