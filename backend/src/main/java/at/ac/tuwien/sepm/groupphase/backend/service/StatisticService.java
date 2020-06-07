package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StatisticDto;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface StatisticService {

    /**
     * creates a turnover statistic involving all persisted data and containing data about turnover.
     *
     * @return created statistic.
     */
    StatisticDto overallTurnover();

    /**
     * creates a sales statistic involving all persisted data and containing data about sales.
     *
     * @return created statistic.
     */
    StatisticDto overallSales();

    /**
     * creates a statistic involving all persisted data from the timerange between from and to
     * and containing data about turnover.
     *
     * @param from start of the timerange, the statistic is about
     * @param to end of the timerange, the statistic is about
     * @return created statistic.
     */
    StatisticDto timedTurnoverStat(Date from, Date to);

    /**
     * creates a statistic involving all persisted data from the timerange between from and to
     * and containing data about sales.
     *
     * @param from start of the timerange, the statistic is about
     * @param to end of the timerange, the statistic is about
     * @return created statistic.
     */
    StatisticDto timedSalesStat(Date from, Date to);
}
