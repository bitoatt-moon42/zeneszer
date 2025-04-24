import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {ReactiveFormsModule, FormControl, FormGroup, Validators} from '@angular/forms';
import {InputRowComponent} from 'app/common/input-row/input-row.component';
import {InstrumentService} from 'app/instrument/instrument.service';
import {InstrumentDTO} from 'app/instrument/instrument.model';
import {ErrorHandler} from 'app/common/error-handler.injectable';
import {updateForm, validNumeric} from 'app/common/utils';


@Component({
  selector: 'app-instrument-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './instrument-edit.component.html'
})
export class InstrumentEditComponent implements OnInit {

  instrumentService = inject(InstrumentService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  currentId?: string;
  type?: string;

  editForm: any;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@instrument.update.success:Instrument was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = this.route.snapshot.params['id'];
    this.type = this.route.snapshot.params['type'];
    this.instrumentService.getInstrument(this.currentId!)
      .subscribe({
        next: (data) => updateForm(this.editForm, data),
        error: (error) => this.errorHandler.handleServerError(error.error)
      });
    this.editForm = new FormGroup({
      id: new FormControl({value: null, disabled: true}),
      type: new FormControl(this.type ? this.type.toUpperCase(): null, [Validators.required]),
      brand: new FormControl(null, [Validators.required, Validators.maxLength(30)]),
      name: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
      price: new FormControl(null, [Validators.required, validNumeric(10, 2)]),
      imageUrl: new FormControl(null, [Validators.maxLength(2048)])
    }, {updateOn: 'submit'});
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.editForm.markAllAsTouched();
    if (!this.editForm.valid) {
      return;
    }
    const data = new InstrumentDTO(this.editForm.value);
    this.instrumentService.updateInstrument(this.currentId!, data)
      .subscribe({
        next: () => this.router.navigate(['/instruments', this.type], {
          state: {
            msgSuccess: this.getMessage('updated')
          }
        }),
        error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
      });
  }

  getBackToListUrl(): string {
    if (this.type == undefined) {
      return '/instruments';
    } else {
      return '/instruments/' + this.type;
    }
  }

  getFormTitle(): string {
    if (this.type === 'guitar') {
      return 'Edit Guitar'
    }
    if (this.type === 'bass_guitar') {
      return 'Edit Bass Guitar'
    }
    if (this.type === 'drum') {
      return 'Edit Drum'
    }
    if (this.type === 'keyboard') {
      return 'Edit Keyboard'
    }
    if (this.type === 'microphone') {
      return 'Edit Microphone'
    }
    return 'Edit Instrument'
  }

}
