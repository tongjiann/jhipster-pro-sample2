import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FileDocxService } from './docx.service';

@Component({
  selector: 'file-docx',
  templateUrl: './docx.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FileDocxComponent {
  result!: string;
  type = 1;
  data = `{
  "name": "cipchk",
  "email": "cipchk@qq.com",
  "description": "1234567890中文",
  "products": [
    {
      "title": "华为",
      "price": 10000
    },
    {
      "title": "小米",
      "price": 999
    }
  ]
}`;
  url = './assets/data/docx.docx';
  file!: File;
  html = ``;

  constructor(private docxSrv: FileDocxService) {}

  onChange(e: Event): void {
    this.file = (e.target as HTMLInputElement)!.files![0];
  }

  output(): void {
    const { type, file, url, data } = this;
    this.docxSrv.docxTemplate(type === 1 ? url : file, JSON.parse(data));
  }

  htmlToDocx(): void {
    this.docxSrv.htmlToDocx(this.html);
  }
}
