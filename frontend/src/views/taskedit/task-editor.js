import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-layout.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-item.js';
import '@vaadin/vaadin-combo-box/src/vaadin-combo-box.js';
import '@vaadin/vaadin-date-picker/src/vaadin-date-picker.js';
import '../../components/buttons-bar.js';
import '../../components/utils-mixin.js';
import '../../../styles/shared-styles.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';
class TaskEditor extends window.ScrollShadowMixin(PolymerElement) {
  static get template() {
    return html`
    <style include="shared-styles">
      :host {
        display: flex;
        flex-direction: column;
        flex: auto;
      }

      .meta-row {
        display: flex;
        justify-content: space-between;
        padding-bottom: var(--lumo-space-s);
      }

      .dim {
        color: var(--lumo-secondary-text-color);
        text-align: right;
        white-space: nowrap;
        line-height: 2.5em;
      }

      .status {
        width: 10em;
      }
    </style>

    <div class="scrollable flex1" id="main">
      <h2 id="taskTitle">New Task</h2>

      <div class="meta-row" id="metaContainer">
        <vaadin-combo-box class="status" id="status"></vaadin-combo-box>

      </div>

      <vaadin-form-layout id="form1">

        <vaadin-form-layout id="form2">
          <vaadin-date-picker label="Due" id="dueDate">
          </vaadin-date-picker>
          <vaadin-combo-box id="dueTime">
            <iron-icon slot="prefix" icon="vaadin:clock"></iron-icon>
          </vaadin-combo-box>
        </vaadin-form-layout>

        <vaadin-form-layout id="form3" colspan="3">
          <vaadin-text-field id="title" label="Title" colspan="2">
            <iron-icon slot="prefix" icon="vaadin:user"></iron-icon>
          </vaadin-text-field>


          <vaadin-text-field id="description" label="Description" colspan="2"></vaadin-text-field>

          <vaadin-combo-box label="Priority" id="priority" colspan="2"></vaadin-combo-box>

          <vaadin-text-field id="users" label="Add Users" colspan="2"></vaadin-text-field>

<multi-select-combo-box items='["Item 1", "Item 2", "Item 3", "Item 4"]' label="Add Users" colspan="2"></multi-select-combo-box>

        </vaadin-form-layout>

      </vaadin-form-layout>
    </div>

    <buttons-bar id="footer" no-scroll\$="[[noScroll]]">
      <vaadin-button slot="left" id="cancel">Cancel</vaadin-button>

      <vaadin-button slot="right" id="review" theme="primary">
        Review Task
        <iron-icon icon="vaadin:arrow-right" slot="suffix"></iron-icon>
      </vaadin-button>
    </buttons-bar>
`;
  }

  static get is() {
    return 'task-editor';
  }

  static get properties() {
    return {
      status: {
        type: String,
        observer: '_onStatusChange'
      }
    };
  }

  ready() {
    super.ready();

    // Not using attributes since Designer does not suppor single-quote attributes
    this.$.form1.responsiveSteps = [
      {columns: 1, labelsPosition: 'top'},
      {minWidth: '600px', columns: 4, labelsPosition: 'top'}
    ];
    this.$.form2.responsiveSteps = [
      {columns: 1, labelsPosition: 'top'},
      {minWidth: '360px', columns: 2, labelsPosition: 'top'}
    ];
    this.$.form3.responsiveSteps = [
      {columns: 1, labelsPosition: 'top'},
      {minWidth: '500px', columns: 3, labelsPosition: 'top'}
    ];
  }

  _onStatusChange() {
    const status = this.status ? this.status.toLowerCase() : this.status;
    this.$.status.$.input.setAttribute('status', status);
  }
}

window.customElements.define(TaskEditor.is, TaskEditor);
