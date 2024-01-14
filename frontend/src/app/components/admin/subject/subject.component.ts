import { Component, Input } from '@angular/core';
import { Subject } from 'src/app/interfaces/subject.interface';

@Component({
  selector: 'subject',
  templateUrl: './subject.component.html',
  styleUrls: ['./subject.component.scss']
})
export class SubjectComponent {

  @Input() 
  public subject?: Subject = undefined;

}
