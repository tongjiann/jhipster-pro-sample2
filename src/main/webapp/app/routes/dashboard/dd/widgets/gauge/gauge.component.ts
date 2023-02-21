import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { _HttpClient } from '@delon/theme';
import { DDBaseWidget } from '../base.widget';

@Component({
  selector: 'dd-widget-gauge',
  templateUrl: './gauge.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardDDWidgetGaugeComponent extends DDBaseWidget implements OnInit, OnDestroy {
  private time: any;
  percent!: number;
  color!: string;

  load(): Promise<void> {
    return new Promise(resolve => {
      setTimeout(() => {
        this.percent = parseInt((Math.random() * 100).toString(), 10);
        this.color = this.percent > 50 ? '#f50' : '#2f9cff';
        this.cdr.detectChanges();
        resolve();
      }, 500);
    });
  }

  ngOnInit(): void {
    // eslint-disable-next-line @typescript-eslint/no-misused-promises
    this.time = setInterval(() => this.load(), 5000);
  }

  ngOnDestroy(): void {
    clearInterval(this.time);
  }
}
