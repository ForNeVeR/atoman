AtomanView = require './atoman-view'
{CompositeDisposable} = require 'atom'

module.exports = Atoman =
  atomanView: null
  subscriptions: null

  activate: (state) ->
    @atomanView = new AtomanView(state.atomanViewState)

    # Events subscribed to in atom's system can be easily cleaned up with a CompositeDisposable
    @subscriptions = new CompositeDisposable

    # Register command that toggles this view
    @subscriptions.add atom.commands.add 'atom-workspace', 'atoman:start': => @start()

  deactivate: ->
    @modalPanel.destroy()
    @subscriptions.dispose()
    @atomanView.destroy()

  serialize: ->
    atomanViewState: @atomanView.serialize()

  start: ->
    # TODO: Start the game
