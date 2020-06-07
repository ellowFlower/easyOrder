package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StatisticDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.StatisticService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/v1/statistic")
public class StatisticEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final StatisticService statisticService;

    @Autowired
    public StatisticEndpoint(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/turnover/overall")
    @ApiOperation(value = "Get overall turnover statistics.", authorizations = {@Authorization(value = "apiKey")})
    public StatisticDto overallTurnover() {
        LOGGER.info("GET /api/v1/statistic/turnover/overall");
        try {
            return statisticService.overallTurnover();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error while creating turnover statistic.");
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/sales/overall")
    @ApiOperation(value = "Get overall sales statistics.", authorizations = {@Authorization(value = "apiKey")})
    public StatisticDto overallSales() {
        LOGGER.info("GET /api/v1/statistic/sales/overall");
        try {
            return statisticService.overallSales();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error while creating sales statistic.");
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/turnover")
    @ApiOperation(value = "Get turnover statistics in a given range of time.", authorizations = {@Authorization(value = "apiKey")})
    public StatisticDto timedTurnoverStat(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                  @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        // time of the dates are 1 in the morning
        // time has to be adapted in milliseconds
        //from.setTime(from.getTime() + (1000 * 60 * 60));
        to.setTime(to.getTime() + (1000 * 60 * 60 * 24 - 1));
        LOGGER.info("GET /api/v1/turnover/statistic from {} to {}", from, to);
        try {
            return statisticService.timedTurnoverStat(from, to);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error while creating timed turnover statistic.");
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/sales")
    @ApiOperation(value = "Get sales statistics in a given range of time.", authorizations = {@Authorization(value = "apiKey")})
    public StatisticDto timedSalesStat(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                          @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        // time of the dates are 1 in the morning
        // time has to be adapted in milliseconds
        //from.setTime(from.getTime() - (1000 * 60 * 60));
        to.setTime(to.getTime() + (1000 * 60 * 60 * 24 - 1));
        LOGGER.info("GET /api/v1/sales/statistic from {} to {}", from, to);
        try {
            return statisticService.timedSalesStat(from, to);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error while creating timed sales statistic.");
        }
    }
}
