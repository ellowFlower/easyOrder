import {Component, ElementRef, Injectable, OnInit, ViewChild} from '@angular/core';
import {Statistic} from '../../dtos/statistic';
import {StatisticService} from '../../services/statistic.service';
import {NgbCalendar, NgbDate, NgbDateParserFormatter} from '@ng-bootstrap/ng-bootstrap';
import {isNumeric} from 'rxjs/internal-compatibility';

@Injectable()
export class NgbDateCustomParserFormatter extends NgbDateParserFormatter {
  parse(value: string): NgbDate {
    console.log('start parsing.');
    if (value) {
      console.log(value);
      const dateParts = value.trim().split('.');
      console.log(dateParts);
      if (dateParts.length === 3 && isNumeric(dateParts[0]) && isNumeric(dateParts[1]) && isNumeric(dateParts[2])) {
        console.log('so far...');
        return new NgbDate(Number(dateParts[2]), Number(dateParts[1]), Number(dateParts[0]));
      }
    }
    console.log('didnt work...');
    return null;
  }

  format(date: NgbDate): string {
    if (date === null) {
      return '';
    } else {
      return ('' + this.pad('' + date.day, 2) + '.' + this.pad('' + date.month, 2) + '.' + date.year);
    }
  }

  pad(date: string, size: number): string{
    while (date.length < size){
      date = '0' + date;
    }
    return date;
  }
}

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.scss']
})
export class StatisticComponent implements OnInit {

  statisticTurnover: Statistic = null;
  statisticSales: Statistic = null;
  error: boolean = false;
  errorMessage: string = '';

  // settings for advanced pie chart
  resultsTurnover;
  resultsSales;
  view = [1000, 250];
  // scheme: object
  labelTurnover = 'Umsatz insgesamt';
  labelSales = 'verkaufte Gerichte und Getränke';

  // variables for date-range
  hoveredDate: NgbDate;
  fromDate: NgbDate;
  toDate: NgbDate;

  constructor(private statisticService: StatisticService, private calendar: NgbCalendar, public formatter: NgbDateParserFormatter) {
    this.statisticTurnover = new Statistic(new Date, new Date, [], []);
    this.statisticSales = new Statistic(new Date, new Date, [], []);
    this.fromDate = this.convertDateToNgbDate(this.statisticTurnover.start);
    this.toDate = this.convertDateToNgbDate(this.statisticTurnover.end);
  }

  ngOnInit() {
    this.loadOverallStatistics();
  }

  loadOverallStatistics() {
    this.statisticService.overallTurnover().subscribe(
      (statistic: Statistic) => {
        this.statisticTurnover = statistic;
        this.resultsTurnover = this.statisticTurnover.foodStats.concat(this.statisticTurnover.drinkStats);
        this.fromDate = this.convertDateToNgbDate(this.statisticTurnover.start);
        this.toDate = this.convertDateToNgbDate(this.statisticTurnover.end);
        },
      error => {
        this.errorMessage = 'Die Statistik zu allen Bestellungen konnte nicht geladen werden.';
        this.error = true;
      }
    );

    this.statisticService.overallSales().subscribe(
      (statistic: Statistic) => {
        this.statisticSales = statistic;
        this.resultsSales = this.statisticSales.foodStats.concat(this.statisticSales.drinkStats);
      },
      error => {
        this.errorMessage = 'Die Statistik zu allen Bestellungen konnte nicht geladen werden.';
        this.error = true;
      }
    );
  }

  calculateTimeRangeStatistics() {
    this.resultsTurnover = [];
    this.resultsSales = [];
    const from = new Date(this.fromDate.year, this.fromDate.month, this.fromDate.day);
    const to = new Date(this.toDate.year, this.toDate.month, this.toDate.day);
    this.statisticService.calculateTimeRangeTurnoverStatistic(from, to).subscribe(
      (statistic: Statistic) => {
        this.statisticTurnover = statistic;
        this.resultsTurnover = this.statisticTurnover.foodStats.concat(this.statisticTurnover.drinkStats);
      },
      error => {
        this.errorMessage = 'Die Statistik zum angegebenen Zeitraum konnte nicht erstellt werden. ' +
          'Bitte stellen Sie sicher, dass der Zeitraum nicht zur Gänze in der Zukunft liegt. ' +
          'Bei manueller Eingabe der Daten achten Sie auf die Schreibweise (dd.mm.yyyy).';
        this.error = true;
      }
    );

    this.statisticService.calculateTimeRangeSalesStatistic(from, to).subscribe(
      (statistic: Statistic) => {
        this.statisticSales = statistic;
        this.resultsSales = this.statisticSales.foodStats.concat(this.statisticSales.drinkStats);
      },
      error => {
        this.errorMessage = 'Die Statistik zum angegebenen Zeitraum konnte nicht erstellt werden. ' +
          'Bitte stellen Sie sicher, dass der Zeitraum nicht zur Gänze in der Zukunft liegt. ' +
          'Bei manueller Eingabe der Daten achten Sie auf die Schreibweise (dd.mm.yyyy).';
        this.error = true;      }
    );
    console.log(this.statisticTurnover.start);
    console.log(this.statisticTurnover.end);
    console.log(this.statisticSales.start);
    console.log(this.statisticSales.end);
  }

  private valueFormatting(num: number): string {
    return (num.toFixed(2) + ' €');
  }

  private percentageFormatting(num: number): string {
    return (num.toFixed(0));
  }

  onDateSelection(date: NgbDate) {
    if (!this.fromDate && !this.toDate) {
      this.fromDate = date;
    } else if (this.fromDate && !this.toDate && (date.after(this.fromDate) || date.equals(this.fromDate))) {
      this.toDate = date;
    } else {
      this.toDate = null;
      this.fromDate = date;
    }
  }

  isHovered(date: NgbDate) {
    return this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate);
  }

  isInside(date: NgbDate) {
    return date.after(this.fromDate) && date.before(this.toDate);
  }

  isRange(date: NgbDate) {
    return date.equals(this.fromDate) || date.equals(this.toDate) || this.isInside(date) || this.isHovered(date);
  }

  validateInput(currentValue: NgbDate, input: string): NgbDate {
    const parsed = this.formatter.parse(input);
    return parsed && this.calendar.isValid(NgbDate.from(parsed)) ? NgbDate.from(parsed) : currentValue;
  }

  convertDateToNgbDate(date: Date): NgbDate {
    return new NgbDate(
      new Date(date).getFullYear(),
      new Date(date).getMonth() + 1,
      new Date(date).getDate());
  }

  vanishError() {
    this.errorMessage = '';
    this.error = false;
  }
}


