export class InstrumentDTO {

  constructor(data:Partial<InstrumentDTO>) {
    Object.assign(this, data);
  }

  id?: string|null;
  type?: string|null;
  brand?: string|null;
  name?: string|null;
  price?: string|null;
  imageUrl?: string|null;

}
