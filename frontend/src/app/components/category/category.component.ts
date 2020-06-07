import {ChangeDetectorRef, Component, ElementRef, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Category} from '../../dtos/category';
import {CategoryService} from '../../services/category.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})

export class CategoryComponent implements OnInit {

  @ViewChild('closeUpdateModal', null) closeUpdateModal: ElementRef;

  createForm: FormGroup;
  updateForm: FormGroup;

  submittedCreate: boolean = false;
  submittedUpdate: boolean = false;

  private category: Category[];
  selectedCategory: Category = new Category(0, ' ', '',  false, '', '');

  successText: string = '';
  errorText: string = '';

  constructor(private categoryService: CategoryService, private ngbPaginationConfig: NgbPaginationConfig, private formBuilder: FormBuilder,
              private cd: ChangeDetectorRef, private authService: AuthService) {
    this.createForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      show: ['', [Validators.required]],
      image: ['', [Validators.required]],
      imageContentType: ['', [Validators.required]]
    });
    this.updateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      show: ['', [Validators.required]],
      image: ['', [Validators.required]],
      imageContentType: ['', [Validators.required]]
    });
  }

  selectCategory(category: Category) {
    this.selectedCategory = new Category(category.id, category.name, category.description, category.show, category.image, category.imageContentType);

    this.updateForm.patchValue({'id': category.id});
    this.updateForm.patchValue({'name': category.name});
    this.updateForm.patchValue({'description': category.description});
    this.updateForm.patchValue({'show': category.show});
    this.updateForm.patchValue({'image': category.image});
    this.updateForm.patchValue({'imageContentType': category.imageContentType});
  }

  ngOnInit() {
    this.loadCategories();
  }
  onItemSelect(item: any) {
    console.log(item);
  }
  onSelectAll(items: any) {
    console.log(items);
  }
  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }
  isUser(): boolean {
    return this.authService.getUserRole() === 'USER';
  }
  getCategory(): Category[] {
    return this.category;
  }

  private clearForm() {
    this.createForm.reset();
    this.updateForm.reset();
    this.submittedCreate = false;
    this.submittedUpdate = false;
  }

  private loadCategories() {
    this.categoryService.getAllCategories().subscribe(
      (category: Category[]) => {
        this.category = category;
      },
      error => {
        this.defaultServiceErrorHandling(error, '');
      }
    );
  }

  createCategory() {
    this.submittedCreate = true;
    if (this.createForm.valid) {
      const category: Category = new Category(null,
        this.createForm.controls.name.value,
        this.createForm.controls.description.value,
        this.createForm.controls.show.value,
        this.createForm.controls.image.value,
        this.createForm.controls.imageContentType.value,
      );
      this.sendCreatedCategoryToService(category);
      this.clearForm();
    } else {
      console.log('Invalid input');
    }
  }

  updateCategory() {
    this.submittedUpdate = true;
    if (this.updateForm.valid) {
      const category: Category = new Category(this.selectedCategory.id,
        this.updateForm.controls.name.value,
        this.updateForm.controls.description.value,
        this.updateForm.controls.show.value,
        this.updateForm.controls.image.value,
        this.updateForm.controls.imageContentType.value,
      );

      this.sendUpdatedCategoryToService(category);
      this.clearForm();
    } else {
      console.log('Invalid input');
    }
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file = event.target.files[0];
        if (isImage && !/^image\//.test(file.type)) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {

          this.selectedCategory.imageContentType = file.type;

          this.createForm.patchValue({
            imageContentType: file.type
          });

          this.updateForm.patchValue({
            imageContentType: file.type
          });
          console.log('filetype ' + file.type);
          this.toBase64FromFile(file);
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      () => console.log('blob added'), // sucess
    );
  }

  toBase64FromFile(file) {

    if (file) {
      const reader = new FileReader();
      reader.onload = this._handleReaderLoaded.bind(this);
      reader.readAsBinaryString(file);
    }
  }

  _handleReaderLoaded(readerEvt) {
    const binaryString = readerEvt.target.result;

    // console.log("Fehler: "+ file.type);

    this.selectedCategory.image = btoa(binaryString);

    this.createForm.patchValue({
      image: btoa(binaryString)

    });
    this.updateForm.patchValue({
      image: btoa(binaryString)

    });
    console.log(btoa(binaryString));
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    this.createForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
  }


  sendCreatedCategoryToService(category: Category) {
    this.categoryService.createCategory(category).subscribe(
      () => {
        this.loadCategories();
        this.defaultServiceSuccessHandling('create');
      },
      error => {
        this.defaultServiceErrorHandling(error, 'create');
      }
    );
  }

  sendUpdatedCategoryToService(category: Category) {
    this.categoryService.updateCategory(category).subscribe(
      () => {
        this.loadCategories();
        this.defaultServiceSuccessHandling('update');
      },
      error => {
         this.defaultServiceErrorHandling(error, 'update');
      }
    );
  }

  confirmDeleteCategory(category: Category): void {
    Swal.fire({
      title: 'Kategorie löschen?',
      text: 'Möchten sie ' + category.name + ' wirklich löschen?',
      icon: 'warning',
      confirmButtonText: 'Ja',
      cancelButtonText: 'Abbrechen',
      showCancelButton: true
    }).then(result => {
      if (result.value) {
        this.deleteCategory(category.id);
      }
    });
  }

  updateVarDeleted(category: Category) {
    this.sendUpdatedCategoryToService(category);
  }

  deleteCategory(id: number): void {
    this.categoryService.deleteCategory(id).subscribe(
      () => {
        this.loadCategories();
        this.defaultServiceSuccessHandling('delete');
      },
      error => {
        this.defaultServiceErrorHandling(error, 'delete');
      }
    );
  }


  private defaultServiceErrorHandling(error: any, action: string) {
    if (action === 'update') {
      this.errorText = 'Kategorie konnte nicht aktualisiert werden';
    } else if (action === 'create') {
      this.errorText = 'Kategorie konnte nicht erstellt erstellt';
    } else if (action === 'delete') {
      this.errorText = 'Kategorie konnte nicht gelöscht werden';
    } else if (action === 'get') {
      this.errorText = 'Kategorie konnte nicht geladen werden';
    }
    console.log(error);

    this.closeUpdateModal.nativeElement.click();
    Swal.fire(
      'Es gab ein Problem!',
      this.errorText,
      'question'
    );
  }


  private defaultServiceSuccessHandling(action: String) {

    if (action === 'update') {
      this.successText = 'Kategoorie wurde erfolgreich aktualisiert';
    } else if (action === 'create') {
      this.successText = 'Kategorie wurde erfolgreich erstellt';
    } else if (action === 'delete') {
      this.successText = 'Kategorie wurde erfolgreich gelöscht';
    }

    this.closeUpdateModal.nativeElement.click();
    Swal.fire({
      title: 'Wunderbar!',
      text: this.successText
    });

    console.log('Successfully ' + this.successText + 'category');
  }
}
