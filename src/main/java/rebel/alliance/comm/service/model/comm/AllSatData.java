package rebel.alliance.comm.service.model.comm;

import java.util.List;
import java.util.Objects;

public class AllSatData {
    private List<SatData> satDataList;

    public AllSatData(List<SatData> satDataList) {
        this.satDataList = satDataList;
    }

    public List<SatData> getSatDataList() {
        return satDataList;
    }

    public void setSatDataList(List<SatData> satDataList) {
        this.satDataList = satDataList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllSatData that = (AllSatData) o;
        return Objects.equals(satDataList, that.satDataList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(satDataList);
    }
}
