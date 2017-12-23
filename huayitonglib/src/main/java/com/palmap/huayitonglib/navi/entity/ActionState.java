package com.palmap.huayitonglib.navi.entity;

/**
 * Created by yangjinhuang on 2017/12/20
 */

public enum ActionState {

//    ACTION_STRAIGHT,            //直行,转弯角度15°以内,[0°,15°]
    ACTION_FRONT_LEFT,          //左前方,转弯角度80°以内,(15°,80°]
    ACTION_FRONT_RIGHT,         //右前方,转弯角度80°以内,(15°,80°]
    ACTION_TURN_LEFT,           //左转,转弯角度100°以内,(80°,100°]
    ACTION_TURN_RIGHT,          //右转,转弯角度100°以内,(80°,100°]
    ACTION_BACK_LEFT,           //左后方,转弯角度大于100°,(100°,180°]
    ACTION_BACK_RIGHT,          //右后方,转弯角度大于100°,(100°,180°]
//    ACTION_TURN_BACK,           //掉头,转弯角度大于165°(165,180]
//    ACTION_CONNECT_FACILITY,    //到达连通设施
    ACTION_UPSTAIRS,            //上楼
    ACTION_DOWNSTAIRS,          //下楼
    ACTION_ARRIVE,              //到达
//    ACTION_RESET,               //重新规划导航线
    UNDEFINED,                 //未定义类型
    CHANGE_FLOOR                  //切换楼层（以后不需要的）
    ;


    @Override
    public String toString() {
        String msg = "";
        switch (this){
            case ACTION_FRONT_LEFT:{
                msg = "向左前方移动";
                break;
            }
            case ACTION_TURN_LEFT:{
                msg = "左转";
                break;
            }
            case ACTION_BACK_LEFT:{
                msg = "向左后方移动";
                break;
            }
            case ACTION_FRONT_RIGHT:{
                msg = "向右前方移动";
                break;
            }
            case ACTION_TURN_RIGHT:{
                msg = "右转";
                break;
            }
            case ACTION_BACK_RIGHT:{
                msg = "向右后方移动";
                break;
            }
            case ACTION_UPSTAIRS:{
                msg = "上楼";
                break;
            }
            case ACTION_DOWNSTAIRS:{
                msg = "下楼";
                break;
            }
            case ACTION_ARRIVE:{
                msg = "到达目的地";
                break;
            }
            case CHANGE_FLOOR:{
                msg = "连通设施";
                break;
            }
            default:
                break;
        }
        return msg;
    }

}
