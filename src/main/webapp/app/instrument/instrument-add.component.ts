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
  selector: 'app-instrument-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './instrument-add.component.html'
})
export class InstrumentAddComponent implements OnInit {

  route = inject(ActivatedRoute);

  instrumentService = inject(InstrumentService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  type: any;

  addForm: any;

  ngOnInit() {
    this.type = this.route.snapshot.params['type'];
    this.addForm = new FormGroup({
      type: new FormControl(this.type ? this.type.toUpperCase(): null, [Validators.required]),
      brand: new FormControl(null, [Validators.required, Validators.maxLength(30)]),
      name: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
      price: new FormControl(null, [Validators.required, validNumeric(10, 2)]),
      imageUrl: new FormControl(null, [Validators.maxLength(2048)])
    }, {updateOn: 'submit'});
  }

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@instrument.create.success:Instrument was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new InstrumentDTO(this.addForm.value);
    this.instrumentService.createInstrument(data)
      .subscribe({
        next: () => this.router.navigate(['/instruments', this.type], {
          state: {
            msgSuccess: this.getMessage('created')
          }
        }),
        error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
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
      return 'Add Guitar'
    }
    if (this.type === 'bass_guitar') {
      return 'Add Bass Guitar'
    }
    if (this.type === 'drum') {
      return 'Add Drum'
    }
    if (this.type === 'keyboard') {
      return 'Add Keyboard'
    }
    if (this.type === 'microphone') {
      return 'Add Microphone'
    }
    return 'Add Instrument'
  }
}
