import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';
class TaskPriorityBadge extends PolymerElement {
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

      :host([priority="high"]) #wrapper {
        color: var(--lumo-error-color);
        background: var(--lumo-error-color-10pct);
      }

      :host([priority="medium"]) #wrapper {
        color: var(--lumo-primary-color);
        background: var(--lumo-primary-color-10pct);
      }

      :host([priority="low"]) #wrapper , #wrapper2{
             color: var(--lumo-secondary-text-color);
             background: var(--lumo-primary-color-10pct);
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
      <span>Priority: [[priority]]</span>

    </div>


`;
  }

  static get is() {
    return 'task-priority-badge';
  }

  static get properties() {
      return {
        priority: {
          type: String,
          observer: '_onStatusChanged',
          reflectToAttribute: true
        },
      };
    }

    _onStatusChanged(current) {
      this.priority = current && current.toLowerCase();
    }
}

window.customElements.define(TaskPriorityBadge.is, TaskPriorityBadge);