import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { firstValueFrom } from 'rxjs';
import { ConfirmationDialogComponent } from '../components/confirmation-dialog/confirmation-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class ConfirmationDialogService {
	
  
  constructor(
    private readonly dialog: MatDialog
  ) { }

	public confirm(
		title: string = 'Are you sure',
		text: string = 'Are you sure you want to do this action?',
		yes: string = 'Yes',
		no: string = 'No'
	) {
		const dialog = this.dialog.open(ConfirmationDialogComponent, {
			disableClose: false,
			minWidth: '350px',
			data: {
				title,
				text,
				yes,
				no
			}
		});

		return firstValueFrom(dialog.afterClosed());
	}
}