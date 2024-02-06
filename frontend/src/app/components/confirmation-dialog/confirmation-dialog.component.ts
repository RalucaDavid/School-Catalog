import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ConfirmDialogData } from 'src/app/interfaces/confirmation.interface';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent {
	
  constructor(
		private readonly dialogRef: MatDialogRef<ConfirmationDialogComponent>,
		@Inject(MAT_DIALOG_DATA)
		public readonly data: ConfirmDialogData
	) { }

	public close() {
		this.dialogRef.close(false);
	}

	public submit(confirm: boolean) {
		this.dialogRef.close(confirm);
	}
}
