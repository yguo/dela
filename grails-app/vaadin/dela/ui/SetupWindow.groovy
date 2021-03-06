package dela.ui

import com.vaadin.data.Item
import com.vaadin.data.util.BeanItem
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Component
import com.vaadin.ui.Field
import com.vaadin.ui.Form
import com.vaadin.ui.FormFieldFactory
import com.vaadin.ui.OptionGroup
import com.vaadin.ui.Window
import dela.Setup

import dela.ui.common.EntityForm

/**
 * @author vedi
 * date 07.07.2010
 * time 22:38:13
 */
class SetupWindow extends Window implements FormFieldFactory {

    Setup setup
    def sessionContext

    private def entityForm

    def void attach() {

        super.attach();

        this.caption = i18n('entity.setup.caption', 'setup')

        setup = sessionContext.setup

        def setupItem = new BeanItem(setup)

        entityForm = new EntityForm(formFields: ['activeSubject', 'filterStates', 'filterSubjects'])
        entityForm.formFieldFactory = this
        entityForm.editable = true

        entityForm.data = setupItem
        entityForm.saveHandler = saveSetup

        this.addComponent(entityForm)

        this.layout.setSizeUndefined()
        this.center()

        entityForm.layout.components[0].focus()
    }

    def saveSetup = {item ->
        sessionContext.storeService.saveSetup(setup)
    }

    Field createField(Item item, Object propertyId, Component component) {
        String caption = i18n("entity.setup.field.${propertyId}.label", propertyId)

        if ('activeSubject'.equals(propertyId)) {
            def comboBox = new ComboBox(caption: caption, immediate: true)
            sessionContext.storeService.getSubjects().each {
                comboBox.addItem it
            }

            comboBox

        } else if ('filterSubjects'.equals(propertyId)) {
            def select = new OptionGroup(caption: caption, immediate: true, multiSelect:true)
            sessionContext.storeService.getSubjects().each {
                select.addItem it
            }

            select

        } else if ('filterStates'.equals(propertyId)) {
            def select = new OptionGroup(caption: caption, immediate: true, multiSelect:true)
            sessionContext.storeService.getStates().each {
                select.addItem it
            }

            select

        } else {
            assert false
        }
    }
}
