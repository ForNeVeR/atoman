AtomanView = require './atoman-view'
{CompositeDisposable} = require 'atom'

module.exports = Atoman =
  atomanView: null
  modalPanel: null
  subscriptions: null

  activate: (state) ->
    @atomanView = new AtomanView(state.atomanViewState)
    @modalPanel = atom.workspace.addModalPanel(item: @atomanView.getElement(), visible: false)

    # Events subscribed to in atom's system can be easily cleaned up with a CompositeDisposable
    @subscriptions = new CompositeDisposable

    # Register command that toggles this view
    @subscriptions.add atom.commands.add 'atom-workspace', 'atoman:toggle': => @toggle()

  deactivate: ->
    @modalPanel.destroy()
    @subscriptions.dispose()
    @atomanView.destroy()

  serialize: ->
    atomanViewState: @atomanView.serialize()

  toggle: ->
    console.log 'Atoman was toggled!'

    if @modalPanel.isVisible()
      @modalPanel.hide()
    else
      @modalPanel.show()
