import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, NavigationEnd, Router, RouterLink} from '@angular/router';
import {Subscription} from 'rxjs';
import {ErrorHandler} from 'app/common/error-handler.injectable';
import {InstrumentService} from 'app/instrument/instrument.service';
import {InstrumentDTO} from 'app/instrument/instrument.model';
import {UserService} from "../user/user.service";
import {UserDTO} from "../user/user.model";


@Component({
  selector: 'app-instrument-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './instrument-list.component.html'
})
export class InstrumentListComponent implements OnInit, OnDestroy {

  instrumentService = inject(InstrumentService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  userService = inject(UserService);
  instruments?: InstrumentDTO[];
  navigationSubscription?: Subscription;

  user?: UserDTO;
  type: any;

  constructor(
    private route: ActivatedRoute
  ) {
  }

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@instrument.delete.success:Instrument was removed successfully.`,
      'instrument.shoppingCartItem.instrument.referenced': $localize`:@@instrument.shoppingCartItem.instrument.referenced:This entity is still referenced by Shopping Cart Item ${details?.id} via field Instrument.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.userService.checkUser()
      .subscribe(data => this.user = data);
    this.type = this.route.snapshot.params['type'];
    this.loadData(this.type);
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData(this.route.snapshot.queryParamMap.get('type'));
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }

  loadData(type: string | null) {
    this.instrumentService.getAllInstruments(type)
      .subscribe({
        next: (data) => this.instruments = data,
        error: (error) => this.errorHandler.handleServerError(error.error)
      });
  }

  isUserLoggedIn(){
    return this.user != undefined;
  }

  getGridTitle():string{
    if(this.type === 'guitar'){
      return 'Guitars'
    }
    if(this.type === 'bass_guitar'){
      return 'Bass Guitars'
    }
    if(this.type === 'drum'){
      return 'Drums'
    }
    if(this.type === 'keyboard'){
      return 'Keyboards'
    }
    if(this.type === 'microphone'){
      return 'Microphones'
    }
    return 'Instruments'
  }

  getCreateButtonTitle():string{
    if(this.type === 'guitar'){
      return 'Create new Guitar'
    }
    if(this.type === 'bass_guitar'){
      return 'Create new Bass Guitar'
    }
    if(this.type === 'drum'){
      return 'Create new Drum'
    }
    if(this.type === 'keyboard'){
      return 'Create new Keyboard'
    }
    if(this.type === 'microphone'){
      return 'Create new Microphone'
    }
    return 'Create new Instrument'
  }

  confirmDelete(id: string) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.instrumentService.deleteInstrument(id)
      .subscribe({
        next: () => this.router.navigate(['/instruments' + this.getTypeForRedirect() ], {
          state: {
            msgInfo: this.getMessage('deleted')
          }
        }).then(() => {
          window.location.reload();
        }),
        error: (error) => {
          if (error.error?.code === 'REFERENCED') {
            const messageParts = error.error.message.split(',');
            this.router.navigate(['/instruments' + this.type ], {
              state: {
                msgError: this.getMessage(messageParts[0], {id: messageParts[1]})
              }
            });
            return;
          }
          this.errorHandler.handleServerError(error.error)
        }
      });
  }

  getTypeForRedirect():string{
    if(this.type == undefined){
      return '';
    } else {
      return '/' + this.type;
    }
  }

  getTypeForEdit():string{
    if(this.type == undefined){
      return '';
    } else {
      return this.type;
    }
  }

  getEditUrl():string{
    if(this.type == undefined){
      return '/instruments/add';
    } else {
      return '/instruments/add/' + this.type;
    }
  }
}
