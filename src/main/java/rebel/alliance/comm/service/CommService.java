package rebel.alliance.comm.service;

import rebel.alliance.comm.service.model.comm.AllSatData;
import rebel.alliance.comm.service.model.comm.SatData;
import rebel.alliance.comm.service.model.comm.ShipData;

/**
 * Communications service
 *
 */
public interface CommService {

    /**
     * Calculates the ship position and message using data obtained from 3 satellites
     *
     * @param allSatData data from all satellites (distances, messages)
     * @return ship position and message
     */
    ShipData calculateShipData(AllSatData allSatData);

    /**
     * Updates the data for a single satellite
     *
     * @param satData distance and message
     */
    void updateSatData(SatData satData);

    /**
     * Calculates the ship position and message using data obtained from 3 satellites
     * Uses the latest data collected (updateSatData method)
     *
     * @return distance and message
     */
    ShipData calculateShipData();
}
