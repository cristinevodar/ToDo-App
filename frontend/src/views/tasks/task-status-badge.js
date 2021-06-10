import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';
class TaskStatusBadge extends PolymerElement {
  static get template() {
    return html`
    <style>
      #wrapper {
        display: inline-block;
        border-radius: var(--lumo-border-radius);
        background: var(--lumo-shade-10pct);
        color: var(--lumo-secondary-text-color);
        padding: 2px 10px;
        font-size: var(--lumo-font-size-xs);
        text-transform: capitalize;
      }

      :host([status="inprogress"]) #wrapper {
        color: var(--lumo-yellow-color);
        background: var(--lumo-success-color-10pct);
      }

      :host([status="new"]) #wrapper {
        color: var(--lumo-primary-color);
        background: var(--lumo-primary-color-10pct);
      }

      :host([status="finished"]) #wrapper , #wrapper2{
             color: var(--lumo-success-color);
             background: var(--lumo-success-color-10pct);
            }





      :host([small]) #wrapper {
        padding: 0 5px;
      }

      iron-icon {
        --iron-icon-width: 12px;
      }

      :host([small]) iron-icon {
        --iron-icon-width: 8px;
      }
    </style>

    <div id="wrapper">
      <span>status: [[status]]</span>

    </div>


`;
  }

  static get is() {
    return 'task-status-badge';
  }

  static get properties() {
    return {
      status: {
        type: String,
        observer: '_onStatusChanged',
        reflectToAttribute: true
      },
    };
  }

  _onStatusChanged(current) {
    this.status = current && current.toLowerCase();
  }
}

window.customElements.define(TaskStatusBadge.is, TaskStatusBadge);
