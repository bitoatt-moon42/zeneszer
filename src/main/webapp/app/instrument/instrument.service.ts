import {Injectable, inject} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from 'environments/environment';
import {InstrumentDTO} from 'app/instrument/instrument.model';


@Injectable({
  providedIn: 'root',
})
export class InstrumentService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/instruments';

  getAllInstruments(type: string | null) {
    if(type)
    {
      return this.http.get<InstrumentDTO[]>(this.resourcePath + '?type=' + type);
    }
    return this.http.get<InstrumentDTO[]>(this.resourcePath);
  }

  getInstrument(id: string) {
    return this.http.get<InstrumentDTO>(this.resourcePath + '/' + id);
  }

  createInstrument(instrumentDTO: InstrumentDTO) {
    return this.http.post<string>(this.resourcePath, instrumentDTO);
  }

  updateInstrument(id: string, instrumentDTO: InstrumentDTO) {
    return this.http.put<string>(this.resourcePath + '/' + id, instrumentDTO);
  }

  deleteInstrument(id: string) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
