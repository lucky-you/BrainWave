package com.zhowin.brainwave.model;

import java.util.List;

/**
 * 每一条曲线
 */
public class LineIncomeList {


    private List<CompositeIndexBean> compositeIndexGEM;
    private List<CompositeIndexBean> clientAccumulativeRate;
    private List<CompositeIndexBean> compositeIndexShanghai;
    private List<CompositeIndexBean> compositeIndexShenzhen;

    public List<CompositeIndexBean> getCompositeIndexGEM() {
        return compositeIndexGEM;
    }

    public void setCompositeIndexGEM(List<CompositeIndexBean> compositeIndexGEM) {
        this.compositeIndexGEM = compositeIndexGEM;
    }

    public List<CompositeIndexBean> getClientAccumulativeRate() {
        return clientAccumulativeRate;
    }

    public void setClientAccumulativeRate(List<CompositeIndexBean> clientAccumulativeRate) {
        this.clientAccumulativeRate = clientAccumulativeRate;
    }

    public List<CompositeIndexBean> getCompositeIndexShanghai() {
        return compositeIndexShanghai;
    }

    public void setCompositeIndexShanghai(List<CompositeIndexBean> compositeIndexShanghai) {
        this.compositeIndexShanghai = compositeIndexShanghai;
    }

    public List<CompositeIndexBean> getCompositeIndexShenzhen() {
        return compositeIndexShenzhen;
    }

    public void setCompositeIndexShenzhen(List<CompositeIndexBean> compositeIndexShenzhen) {
        this.compositeIndexShenzhen = compositeIndexShenzhen;
    }
}
